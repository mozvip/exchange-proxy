package com.github.mozvip.exchange.core;

public enum ActiveSyncCommand {
	
	SYNC("Sync", false, false, false, false),
	SEND_MAIL("SendMail", false, false, false, false),
	SMART_FORWARD("SmartForward", false, false, false, false),
	SMART_REPLY("SmartReply", false, false, false, false),
	GET_ATTACHMENT("GetAttachment", false, false, false, false),
	GET_HIERARCHY("GetHierarchy", false, false, false, false),
	CREATE_COLLECTION("CreateCollection", false, false, false, false),
	DELETE_COLLECTION("DeleteCollection", false, false, false, false),
	MOVE_COLLECTION("MoveCollection", false, false, false, false),
	FOLDER_SYNC("FolderSync", false, false, false, false),
	FOLDER_CREATE("FolderCreate", false, false, false, false),
	FOLDER_DELETE("FolderDelete", false, false, false, false),
	FOLDER_UPDATE("FolderUpdate", false, false, false, false),
	MOVE_ITEMS("MoveItems", false, false, false, false),
	GET_ITEMS_ESTIMATE("GetItemsEstimate", false, false, false, false),
	MEETING_RESPONSE("MeetingResponse", false, false, false, false),
	SEARCH("Search", false, false, false, false),
	SETTINGS("Settings", true, false, true, false),
	PING("Ping", false, false, false, false),
	ITEM_OPERATIONS("ItemOperations", false, false, false, false),
	PROVISION("Provision", true, true, false, false),
	RESOLVE_RECIPIENTS("ResolveRecipients", false, false, false, false),
	VALIDATE_CERT("ValidateCert", false, false, false, false),
	FIND("Find", false, false, false, false);
	
	private String label;

	private boolean requestMustBeModified;
	private boolean responseMustBeModified;
	private boolean requestMustBeDumped;
	private boolean responseMustBeDumped;
	
	ActiveSyncCommand(String label, boolean requestMustBeModified, boolean responseMustBeModified, boolean requestMustBeDumped, boolean responseMustBeDumped) {
		this.label = label;
		this.requestMustBeModified = requestMustBeModified;
		this.responseMustBeModified = responseMustBeModified;
		this.requestMustBeDumped = requestMustBeDumped;
		this.responseMustBeDumped = responseMustBeDumped;
	}
	
	public boolean isRequestMustBeModified() {
		return requestMustBeModified;
	}
	
	public boolean isResponseMustBeModified() {
		return responseMustBeModified;
	}
	
	public boolean isRequestMustBeDumped() {
		return requestMustBeDumped;
	}
	
	public boolean isResponseMustBeDumped() {
		return responseMustBeDumped;
	}
	
	public String getLabel() {
		return label;
	}
	
	public static ActiveSyncCommand from(String label) {
		for (ActiveSyncCommand command : ActiveSyncCommand.values()) {
			if (command.getLabel().equals(label)) {
				return command;
			}
		}
		return null;
	}

}

