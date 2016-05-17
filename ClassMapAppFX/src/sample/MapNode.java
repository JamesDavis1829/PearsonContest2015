package sample;/*
 * MapNode Class.
 * Stores all information for each node in the database.
 */

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.shape.Line;

import java.awt.Image.*;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.*;
import java.sql.*;
import java.util.Date;

/**
 *
 * @author acous
 */
public class MapNode {

    public enum classification {
        image, link, string, topic
    }

    public int uniqueId;
    public int votes;
    private boolean userVote = false;
    public boolean previousVote = false;
    public String createdBy;
    public String nodePerm;
    public ArrayList<MapNode> children = new ArrayList<>();
    public String description = "hello";
    public int parent;
    boolean sinceLastLog;
    public classification type;
    public Timestamp timeCreated;
    int layer;
    MapNode parentNode;
    int quadrant=1;
    int noOfChildren=0;
    int circleNo=1;
    int childLimit=2;
    int expansion =2 ;
    int childno=0;
    int expansionconst= 1;
    int offset =0 ;
    Line parentLine;
    double a;
    double b;

    public Boolean getUserVote()
    {
        return userVote;
    }

    public void setUniqueId(int id) { this.uniqueId = id; }

    public void setUserVote(Boolean value) { this.userVote = value; }

    public void incrementVoteCounter() { this.votes++; }

    public void decrementVoteCounter() { this.votes--; }

    public int getVotes()
    {
        return this.votes;
    }

    public int getParent()
    {
        return this.parent;
    }

    public void AddNode(MapNode addition)
    {
        this.children.add(addition);
    }

    public long getSeconds()
    {
        return this.timeCreated.getTime();
    }

    public String getType() { return this.type.toString(); }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public int getLayer() {
        return layer;
    }

    public MapNode getParentNode() {
        return parentNode;
    }

    public void setParentNode(MapNode parentNode) {
        this.parentNode = parentNode;
    }

    public void setQuadrant(int quadrant) {
        this.quadrant = quadrant;
    }

    public int getQuadrant() {
        return quadrant;
    }

    public void setNoOfChildren(int noOfChildren) {
        this.noOfChildren = noOfChildren;
    }

    public int getNoOfChildren() {
        return noOfChildren;
    }

    public int getCircleNo() {
        return circleNo;
    }

    public void setCircleNo(int circleNo) {
        this.circleNo = circleNo;
    }

    public int getChildLimit() {
        return childLimit;
    }

    public void setChildLimit(int childLimit) {
        this.childLimit = childLimit;
    }

    public int getExpansion() {
        return expansion;
    }

    public void setExpansion(int expansion) {
        this.expansion = expansion;
    }

    public void setChildno(int childno) {
        this.childno = childno;
    }

    public int getChildno() {
        return childno;
    }

    public void setExpansionconst(int expansionconst) {
        this.expansionconst = expansionconst;
    }

    public int getExpansionconst() {
        return expansionconst;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    public double getA() {
        return a;
    }

    public void setA(double a) {
        this.a = a;
    }

    public double getB() {
        return b;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public int getUniqueId() {
        return uniqueId;
    }

    public Line getParentLine() {
        return parentLine;
    }

    public void setParentLine(Line parentLine) {
        this.parentLine = parentLine;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

