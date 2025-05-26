package com.stefan.egovernmentapp.utils;

import org.springframework.stereotype.Component;

@Component
public class EmailUtil {
    public static final String SUBJECT_PENDING_RESIDENT_REQUEST = "Update on Your Resident Request";
    public static final String BODY_PENDING_RESIDENT_REQUEST =
            "Your resident request has been marked as: %s. If approved you can now register as a resident. If rejected, please re-submit your request.";

    public static final String SUBJECT_COMPLAINT_RECEIVED = "We've Received Your Complaint (ID: %s)";
    public static final String BODY_COMPLAINT_RECEIVED = "Thank you for your submission. We've received the following complaint type: %s with the following note: %s. Our team will review it shortly.";

    public static final String SUBJECT_COMPLAINT_STATUS_CHANGED = "Your Complaint Status Has Been Updated (ID: %s)";
    public static final String BODY_COMPLAINT_STATUS_CHANGED = "The status of your complaint has been updated to: %s with the following note from our employee: %s. You can view more information in your account.";
}
