package com.example.admin.partypay.MainActivityFile;

/**
 * This object contain 1 ImageView and 2 TextView
 * describing details of an event in the MainActivity
 */

public class EventBar {

    //Constant
    private static final int NO_IMAGE_PROVIDED = -1;
    public static final int STATUS_NONE = 0;
    public static final int STATUS_FAVOURITE = 1;
    public static final int STATUS_DONE = 2;

    /**
     * @param imageResourceId = id of an event icon
     * @param eventName = String of corresponding event's name
     * @param eventDate = String of event's creation date
     * @param statusBarShown = variable to decide if the status bar will be shown or not
     * @param status = Status of event (0 = none, 1 = favourite, 2 = done)
     */


    private String mEventName;
    private String mEventDate;
    private boolean mStatusBarShown = false;
    private int mStatus = STATUS_NONE;
    private int mImageResourceId = NO_IMAGE_PROVIDED;


    // Constructor
    public EventBar(int imageResourceId, String eventName, String eventDate) {
        mImageResourceId = imageResourceId;
        mEventName = eventName;
        mEventDate = eventDate;
    }

    public EventBar(int imageResourceId, String eventName, String eventDate, boolean statusBarShown) {
        mImageResourceId = imageResourceId;
        mEventName = eventName;
        mEventDate = eventDate;
        mStatusBarShown = statusBarShown;
    }

    // Method to return the ID of event image
    public int getImageResourceId() {
        return mImageResourceId;
    }

    // Method to return the event's name
    public String getEventName() {
        return mEventName;
    }

    // Method to return the event's date
    public String getEventDate() {
        return mEventDate;
    }

    // Method to return if the status bar need to be shown
    public boolean getStatusBarShown() {
        return mStatusBarShown;
    }

    // In case no image ID is passed in
    public boolean isImageResourceIdValid() {
        return mImageResourceId != NO_IMAGE_PROVIDED;
    }

    // Use when you need to statusBar need to be switched
    public void toggleStatusBarShown() {
        mStatusBarShown = !mStatusBarShown;
    }

    // Set the new status for an event
    public int setStatus(int newStatus){
        mStatus = newStatus;
        return mStatus;
    }

    public int getStatus(){
        return mStatus;
    }

    //Use for debug: Check at android monitor
    @Override
    public String toString() {
        return "EventBar method {" +
                "Image ID: " + mImageResourceId + "\'" +
                "Event Name: " + mEventName + "\'" +
                "Event Date: " + mEventDate + "\'" +
                "Does status bar currently shown?:" + mStatusBarShown + "\'" +
                "Event status:" + mStatus +
                '}';
    }
}
