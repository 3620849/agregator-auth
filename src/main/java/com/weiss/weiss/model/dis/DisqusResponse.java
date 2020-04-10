package com.weiss.weiss.model.dis;

import lombok.Data;

@Data
public class DisqusResponse {
    private boolean isFollowing;
    private boolean disable3rdPartyTrackers;
    private boolean isPowerContributor;
    private boolean isFollowedBy;
    private boolean isPrimary;
    private String id;
    private float numFollowers;
    private float rep;
    private float numFollowing;
    private float numPosts;
    private String location;
    private boolean isPrivate;
    private String joinedAt;
    private String username;
    private float numLikesReceived;
    private String reputationLabel;
    private String about;
    private String name;
    private String url;
    private boolean isBlocked;
    private float numForumsFollowing;
    private String profileUrl;
    private float reputation;
    Avatar avatar;
    private String signedUrl;
    private boolean isAnonymous;
}
