package com.example.betabreaker.Classes;

public class ClsComment {
    private String username;
    private String CommentCont;

    public ClsComment(String username, String CommentCont){
        this.username = username;
        this.CommentCont = CommentCont;
    }

    public String getCommentCont() {return CommentCont;}

    public String getUsername() {return username;}

    public void setCommentCont(String commentCont) {CommentCont = commentCont;}

    public void setUsername(String username) {this.username = username;}
}
