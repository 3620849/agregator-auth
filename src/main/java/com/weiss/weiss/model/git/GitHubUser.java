package com.weiss.weiss.model.git;

import com.weiss.weiss.model.Plan;
import lombok.Data;

@Data
public class GitHubUser {

        private String login;
        private float id;
        private String node_id;
        private String avatar_url;
        private String gravatar_id;
        private String url;
        private String html_url;
        private String followers_url;
        private String following_url;
        private String gists_url;
        private String starred_url;
        private String subscriptions_url;
        private String organizations_url;
        private String repos_url;
        private String events_url;
        private String received_events_url;
        private String type;
        private boolean site_admin;
        private String name;
        private String company ;
        private String blog;
        private String location ;
        private String email;
        private String hireable;
        private String bio;
        private float public_repos;
        private float public_gists;
        private float followers;
        private float following;
        private String created_at;
        private String updated_at;
        private float private_gists;
        private float total_private_repos;
        private float owned_private_repos;
        private float disk_usage;
        private float collaborators;
        private boolean two_factor_authentication;
        Plan plan;
}
