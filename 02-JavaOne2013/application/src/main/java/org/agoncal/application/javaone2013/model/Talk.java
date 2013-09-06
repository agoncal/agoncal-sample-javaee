package org.agoncal.application.javaone2013.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Antonio Goncalves
 *         http://www.antoniogoncalves.org
 *         --
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "findAllTalks", query = "SELECT t FROM Talk t")
})
@XmlRootElement
public class Talk implements Serializable {

    // ======================================
    // =             Attributes             =
    // ======================================

    @Id
    @GeneratedValue
    private Long id;
    @Version
    private int version;
    @NotNull
    private String title;
    private String description;
    @NotNull
    private String room;
    @Temporal(TemporalType.DATE)
    private Date date;
    @OneToMany
    private Set<Speaker> speakers = new HashSet<>();

    // ======================================
    // =            Constructors            =
    // ======================================

    public Talk() {
    }

    public Talk(String title, String description, String room, Date date) {
        this.title = title;
        this.description = description;
        this.room = room;
        this.date = date;
    }

    // ======================================
    // =          Getters & Setters         =
    // ======================================


    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Set<Speaker> getSpeakers() {
        return speakers;
    }

    public void setSpeakers(Set<Speaker> speakers) {
        this.speakers = speakers;
    }
}