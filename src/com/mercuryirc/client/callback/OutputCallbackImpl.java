package com.mercuryirc.client.callback;

import com.mercuryirc.client.Mercury;
import com.mercuryirc.client.ui.ApplicationPane;
import com.mercuryirc.client.ui.Tab;
import com.mercuryirc.client.ui.model.MessageRow;
import com.mercuryirc.model.Channel;
import com.mercuryirc.model.Message;
import com.mercuryirc.model.Server;
import com.mercuryirc.model.User;
import com.mercuryirc.network.Connection;
import com.mercuryirc.network.callback.OutputCallback;
import javafx.application.Platform;

public class OutputCallbackImpl implements OutputCallback {

	private final ApplicationPane appPane;

	public OutputCallbackImpl(ApplicationPane appPane) {
		this.appPane = appPane;
	}

	@Override
	public void onPrivmsg(final Connection connection, final Message message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				appPane.getTabPane().addTargetedMessage(connection, message, MessageRow.Type.SELF);
			}
		});
	}

	@Override
	public void onNotice(final Connection connection, final Message message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				appPane.getTabPane().addUntargetedMessage(connection, message, MessageRow.Type.NOTICE);
			}
		});
	}

	@Override
	public void onCtcp(final Connection connection, final Message message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				String ctcp = message.getMessage();
				if (ctcp.startsWith("ACTION")) {
					Message _message = new Message(connection.getLocalUser(), message.getTarget(), ctcp.substring(7));
					appPane.getTabPane().addTargetedMessage(connection, _message, MessageRow.Type.EVENT);
				} else {
					appPane.getTabPane().addUntargetedMessage(connection, message, MessageRow.Type.CTCP);
				}
			}
		});
	}

	@Override
	public void onQuery(final Connection connection, final User user) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Tab tab = appPane.getTabPane().create(connection, user);
				appPane.getTabPane().select(tab);
			}
		});
	}

	@Override
	public void onJoin(final Connection connection, final Channel channel) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Tab tab = appPane.getTabPane().get(connection, channel);
				appPane.getTabPane().select(tab);
			}
		});
	}

	@Override
	public void onConnectionRequest(final Connection connection, final String network, final String hostname, final int port, final String nick) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				Server server = new Server(network, hostname, port, "", false);
				User local = connection.getLocalUser();
				User user = new User(server, local.getName(), local.getUserName(), local.getRealName());
				Mercury.connect(server, user);
			}
		});
	}

}