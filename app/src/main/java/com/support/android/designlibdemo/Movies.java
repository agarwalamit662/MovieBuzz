package com.support.android.designlibdemo;

import com.support.android.designlibdemo.data.UserProvider;

/**
 * Created by Amit on 27-06-2015.
 */
public class Movies {


    private String movieid;
    private String mname;
    private String reldate;
    private String watcher;
    private String des;
    private String url;
    private String iswatched;
    private String likes;
    private String iswatchlist;
    private String category;
    private String rating;
    private String isreviewed;
    private String review;
    private String movieonparse;
    private String moviereviewtime;

    public Movies(String movieid, String mname, String reldate, String watcher, String des, String url, String iswatched, String likes, String iswatchlist, String category,  String rating,String isreviewed, String review,String movieonparse,String moviereviewtime) {
        this.movieid = movieid;
        this.mname = mname;
        this.reldate = reldate;
        this.watcher = watcher;
        this.des = des;
        this.url = url;
        this.iswatched = iswatched;
        this.likes = likes;
        this.iswatchlist = iswatchlist;
        this.category = category;
        this.isreviewed = isreviewed;
        this.rating = rating;
        this.review = review;
        this.movieonparse = movieonparse;
        this.moviereviewtime = moviereviewtime;
    }

    public String getMoviereviewtime() {
        return moviereviewtime;
    }

    public void setMoviereviewtime(String moviereviewtime) {
        this.moviereviewtime = moviereviewtime;
    }

    public String getMovieonparse() {
        return movieonparse;
    }

    public void setMovieonparse(String movieonparse) {
        this.movieonparse = movieonparse;
    }

    public String getMovieid() {
        return movieid;
    }

    public void setMovieid(String movieid) {
        this.movieid = movieid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getReldate() {
        return reldate;
    }

    public void setReldate(String reldate) {
        this.reldate = reldate;
    }

    public String getWatcher() {
        return watcher;
    }

    public void setWatcher(String watcher) {
        this.watcher = watcher;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIswatched() {
        return iswatched;
    }

    public void setIswatched(String iswatched) {
        this.iswatched = iswatched;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getIswatchlist() {
        return iswatchlist;
    }

    public void setIswatchlist(String iswatchlist) {
        this.iswatchlist = iswatchlist;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getIsreviewed() {
        return isreviewed;
    }

    public void setIsreviewed(String isreviewed) {
        this.isreviewed = isreviewed;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}

