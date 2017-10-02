package com.github.mozvip.exchange.wbxml;

public class WbxmlEASPages {

	static public String[][] pages = { { // 0x00 AirSync
			"Sync", "Responses", "Add", "Change", "Delete", "Fetch", "SyncKey", "ClientId", "ServerId", "Status",
			"Collection", "Class", "Version", "CollectionId", "GetChanges", "MoreAvailable", "WindowSize", "Commands",
			"Options", "FilterType", "Truncation", "RTFTruncation", "Conflict", "Collections", "ApplicationData",
			"DeletesAsMoves", "NotifyGUID", "Supported", "SoftDelete", "MIMESupport", "MIMETruncation", "Wait", "Limit",
			"Partial", "ConversationMode", "MaxItems", "HeartbeatInterval" },
			{
					// 0x01 Contacts
					"Anniversary", "AssistantName", "AssistantTelephoneNumber", "Birthday", "ContactsBody",
					"ContactsBodySize", "ContactsBodyTruncated", "Business2TelephoneNumber", "BusinessAddressCity",
					"BusinessAddressCountry", "BusinessAddressPostalCode", "BusinessAddressState",
					"BusinessAddressStreet", "BusinessFaxNumber", "BusinessTelephoneNumber", "CarTelephoneNumber",
					"ContactsCategories", "ContactsCategory", "Children", "Child", "CompanyName", "Department",
					"Email1Address", "Email2Address", "Email3Address", "FileAs", "FirstName", "Home2TelephoneNumber",
					"HomeAddressCity", "HomeAddressCountry", "HomeAddressPostalCode", "HomeAddressState",
					"HomeAddressStreet", "HomeFaxNumber", "HomeTelephoneNumber", "JobTitle", "LastName", "MiddleName",
					"MobileTelephoneNumber", "OfficeLocation", "OtherAddressCity", "OtherAddressCountry",
					"OtherAddressPostalCode", "OtherAddressState", "OtherAddressStreet", "PagerNumber",
					"RadioTelephoneNumber", "Spouse", "Suffix", "Title", "Webpage", "YomiCompanyName", "YomiFirstName",
					"YomiLastName", "CompressedRTF", "Picture" },
			{
					// 0x02 Email
					"Attachment", "Attachments", "AttName", "AttSize", "Add0Id", "AttMethod", "AttRemoved", "Body",
					"BodySize", "BodyTruncated", "DateReceived", "DisplayName", "DisplayTo", "Importance",
					"MessageClass", "Subject", "Read", "To", "CC", "From", "ReplyTo", "AllDayEvent", "Categories",
					"Category", "DTStamp", "EndTime", "InstanceType", "IntDBusyStatus", "Location", "MeetingRequest",
					"Organizer", "RecurrenceId", "Reminder", "ResponseRequested", "Recurrences", "Recurence",
					"Recurrence_Type", "Recurrence_Until", "Recurrence_Occurrences", "Recurrence_Interval",
					"Recurrence_DayOfWeek", "Recurrence_DayOfMonth", "Recurrence_WeekOfMonth", "Recurrence_MonthOfYear",
					"StartTime", "Sensitivity", "TimeZone", "GlobalObjId", "ThreadTopic", "MIMEData", "MIMETruncated",
					"MIMESize", "InternetCPID", "Flag", "FlagStatus", "EmailContentClass", "FlagType", "CompleteTime",
					"DisallowNewTimeProposal" },
			{
			// 0x03 AirNotify
			}, {
					// 0x04 Calendar
					"CalTimeZone", "CalAllDayEvent", "CalAttendees", "CalAttendee", "CalAttendee_Email",
					"CalAttendee_Name", "CalBody", "CalBodyTruncated", "CalBusyStatus", "CalCategories", "CalCategory",
					"CalCompressed_RTF", "CalDTStamp", "CalEndTime", "CalExeption", "CalExceptions",
					"CalException_IsDeleted", "CalException_StartTime", "CalLocation", "CalMeetingStatus",
					"CalOrganizer_Email", "CalOrganizer_Name", "CalRecurrence", "CalRecurrence_Type",
					"CalRecurrence_Until", "CalRecurrence_Occurrences", "CalRecurrence_Interval",
					"CalRecurrence_DayOfWeek", "CalRecurrence_DayOfMonth", "CalRecurrence_WeekOfMonth",
					"CalRecurrence_MonthOfYear", "CalReminder_MinsBefore", "CalSensitivity", "CalSubject",
					"CalStartTime", "CalUID", "CalAttendee_Status", "CalAttendee_Type", "CalAttachment",
					"CalAttachments", "CalAttName", "CalAttSize", "CalAttOid", "CalAttMethod", "CalAttRemoved",
					"CalDisplayName", "CalDisallowNewTimeProposal", "CalResponseRequested", "CalAppointmentReplyTime",
					"CalResponseType", "CalCalendarType", "CalIsLeapMonth", "CalFirstDayOfWeek",
					"CalOnlineMeetingConfLink", "CalOnlineMeetingExternalLink" },
			{
					// 0x05 Move
					"MoveItems", "Move", "SrcMsgId", "SrcFldId", "DstFldId", "MoveResponse", "MoveStatus", "DstMsgId" },
			{
					// 0x06 ItemEstimate
					"GetItemEstimate", "Version", "Collections", "Collection", "Class", "CollectionId", "DateTime",
					"Estimate", "Response", "Status" },
			{
					// 0x07 FolderHierarchy
					"Folders", "Folder", "FolderDisplayName", "FolderServerId", "FolderParentId", "Type",
					"FolderResponse", "FolderStatus", "FolderContentClass", "Changes", "FolderAdd", "FolderDelete",
					"FolderUpdate", "FolderSyncKey", "FolderFolderCreate", "FolderFolderDelete", "FolderFolderUpdate",
					"FolderSync", "Count", "FolderVersion" },
			{
					// 0x08 MeetingResponse
					"CalId", "CollectionId", "MeetingResponse", "ReqId", "Request", "Result", "Status", "UserResponse",
					"Version" },
			{
					// 0x09 Tasks
					"Body", "BodySize", "BodyTruncated", "Categories", "Category", "Complete", "DateCompleted",
					"DueDate", "UTCDueDate", "Importance", "Recurrence", "RecurrenceType", "RecurrenceStart",
					"RecurrenceUntil", "RecurrenceOccurrences", "RecurrenceInterval", "RecurrenceDOM", "RecurrenceDOW",
					"RecurrenceWOM", "RecurrenceMOY", "RecurrenceRegenerate", "RecurrenceDeadOccur", "ReminderSet",
					"ReminderTime", "Sensitivity", "StartDate", "UTCStartDate", "Subject", "CompressedRTF",
					"OrdinalDate", "SubordinalDate" },
			{
			// 0x0A ResolveRecipients
			}, {
			// 0x0B ValidateCert
			}, {
					// 0x0C Contacts2
					"CustomerId", "GovernmentId", "IMAddress", "IMAddress2", "IMAddress3", "ManagerName",
					"CompanyMainPhone", "AccountName", "NickName", "MMS" },
			{
					// 0x0D Ping
					"Ping", "AutdState", "PingStatus", "HeartbeatInterval", "PingFolders", "PingFolder", "PingId",
					"PingClass", "MaxFolders" },
			{
					// 0x0E Provision
					"Provision", "Policies", "Policy", "PolicyType", "PolicyKey", "Data", "ProvisionStatus",
					"RemoteWipe", "EASProvidionDoc", "DevicePasswordEnabled", "AlphanumericDevicePasswordRequired",
					"DeviceEncryptionEnabled", "PasswordRecoveryEnabled", "-unused-", "AttachmentsEnabled",
					"MinDevicePasswordLength", "MaxInactivityTimeDeviceLock", "MaxDevicePasswordFailedAttempts",
					"MaxAttachmentSize", "AllowSimpleDevicePassword", "DevicePasswordExpiration",
					"DevicePasswordHistory", "AllowStorageCard", "AllowCamera", "RequireDeviceEncryption",
					"AllowUnsignedApplications", "AllowUnsignedInstallationPackages",
					"MinDevicePasswordComplexCharacters", "AllowWiFi", "AllowTextMessaging", "AllowPOPIMAPEmail",
					"AllowBluetooth", "AllowIrDA", "RequireManualSyncWhenRoaming", "AllowDesktopSync",
					"MaxCalendarAgeFilder", "AllowHTMLEmail", "MaxEmailAgeFilter", "MaxEmailBodyTruncationSize",
					"MaxEmailHTMLBodyTruncationSize", "RequireSignedSMIMEMessages", "RequireEncryptedSMIMEMessages",
					"RequireSignedSMIMEAlgorithm", "RequireEncryptionSMIMEAlgorithm",
					"AllowSMIMEEncryptionAlgorithmNegotiation", "AllowSMIMESoftCerts", "AllowBrowser",
					"AllowConsumerEmail", "AllowRemoteDesktop", "AllowInternetSharing",
					"UnapprovedInROMApplicationList", "ApplicationName", "ApprovedApplicationList", "Hash" },
			{
					// 0x0F Search
					"Search", "Stores", "Store", "Name", "Query", "Options", "Range", "Status", "Response", "Result",
					"Properties", "Total", "EqualTo", "Value", "And", "Or", "FreeText", "SubstringOp", "DeepTraversal",
					"LongId", "RebuildResults", "LessThan", "GreateerThan", "Schema", "Supported" },
			{
					// 0x10 Gal
					"GalDisplayName", "GalPhone", "GalOffice", "GalTitle", "GalCompany", "GalAlias", "GalFirstName",
					"GalLastName", "GalHomePhone", "GalMobilePhone", "GalEmailAddress" },
			{
					// 0x11 AirSyncBase
					"BodyPreference", "BodyPreferenceType", "BodyPreferenceTruncationSize", "AllOrNone", "--unused--",
					"BaseBody", "BaseData", "BaseEstimatedDataSize", "BaseTruncated", "BaseAttachments",
					"BaseAttachment", "BaseDisplayName", "FileReference", "BaseMethod", "BaseContentId",
					"BaseContentLocation", "BaseIsInline", "BaseNativeBodyType", "BaseContentType", "BasePreview", "BodyPartPreference", "BodyPart", "Status" },
			{
					// 0x12 Settings
					"Settings", "SettingsStatus", "Get", "Set", "Oof", "OofState", "SettingsStartTime",
					"SettingsEndTime", "OofMessage", "AppliesToInternal", "AppliesToExternalKnown",
					"AppliesToExternalUnknown", "Enabled", "ReplyMessage", "BodyType", "DevicePassword", "Password",
					"DeviceInformation", "Model", "IMEI", "FriendlyName", "OS", "OSLanguage", "PhoneNumber",
					"UserInformation", "EmailAddress", "StmpAddress", "UserAgent", "EnableOutboundSMS",
					"MobileOperator", "PrimarySmtpAddress", "Accounts", "Account", "AccountId", "AccountName", "UserDisplayName", "SendDisabled", "RightsManagementInformation" },
			{
			// 0x13 DocumentLibrary
			}, {
					// 0x14 ItemOperations
					"Items", "ItemsFetch", "ItemsStore", "ItemsOptions", "ItemsRange", "ItemsTotal", "ItemsProperties",
					"ItemsData", "ItemsStatus", "ItemsResponse", "ItemsVersion", "ItemsSchema", "ItemsPart",
					"ItemsEmptyFolder", "ItemsDeleteSubFolders", "ItemsUserName", "ItemsPassword", "ItemsMove",
					"ItemsDstFldId", "ItemsConversationId", "ItemsMoveAlways" },
			{
					// 0x15 ComposeMail
					"SendMail", "SmartForward", "SmartReply", "SaveInSentItems", "ReplaceMime", "--unused--",
					"ComposeSource", "ComposeFolderId", "ComposeItemId", "ComposeLongId", "ComposeInstanceId",
					"ComposeMime", "ComposeClientId", "ComposeStatus", "ComposeAccountId" },
			{
					// 0x16 Email2
					"UmCallerId", "UmUserNotes", "UmAttDuration", "UmAttOrder", "ConversationId", "ConversationIndex",
					"LastVerbExecuted", "LastVerbExecutionTime", "ReceivedAsBcc", "Sender", "CalendarType",
					"IsLeapMonth", "AccountId", "FirstDayOfWeek", "MeetingMessageType" },
			{
			// 0x17 Notes
			}, {
					// 0x18 Rights Management
					"RMSupport", "RMTemplates", "RMTemplate", "RMLicense", "EditAllowed", "ReplyAllowed",
					"ReplyAllAllowed", "ForwardAllowed", "ModifyRecipientsAllowed", "ExtractAllowed", "PrintAllowed",
					"ExportAllowed", "ProgrammaticAccessAllowed", "RMOwner", "ContentExpiryDate", "TemplateID",
					"TemplateName", "TemplateDescription", "ContentOwner", "RemoveRMDistribution" } };
}
