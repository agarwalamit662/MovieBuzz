package com.support.android.designlibdemo;

import bolts.Bolts;

/**
 * Created by Amit on 27-06-2015.
 */
public class Followers {

    private String fbid;
    private String fbname;
    private boolean fbfollowing;
    private String url;
    public Followers(String id,String name,boolean following,String url){

        this.fbid = id;
        this.fbname = name;
        this.fbfollowing = following;
        this.url = url;

    }

    public String getid(){
        return this.fbid;
    }
    public String getname(){
        return this.fbname;
    }
    public boolean getFollowing(){
        return this.fbfollowing;
    }

    public String getUrlFriend(){return this.url;}
}

