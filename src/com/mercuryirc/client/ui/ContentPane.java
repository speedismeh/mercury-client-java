package com.mercuryirc.client.ui;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ContentPane extends VBox {

	private final TopicPane topicPane;
	private final MessagePane messagePane;
	private final UserPane userPane;

	public ContentPane(ApplicationPane appPane) {
		VBox.setVgrow(this, Priority.ALWAYS);
		HBox.setHgrow(this, Priority.ALWAYS);
		HBox box = new HBox();
		VBox.setVgrow(box, Priority.ALWAYS);
		HBox.setHgrow(box, Priority.ALWAYS);
		box.getChildren().addAll(messagePane = new MessagePane(appPane), userPane = new UserPane(appPane));
		getChildren().addAll(topicPane = new TopicPane(appPane), box);
	}

	public TopicPane getTopicPane() {
		return topicPane;
	}

	public MessagePane getMessagePane() {
		return messagePane;
	}

	public UserPane getUserPane() {
		return userPane;
	}

}