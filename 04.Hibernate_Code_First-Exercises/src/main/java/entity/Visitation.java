package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "visitations")
public class Visitation extends BaseEntity{

    private LocalDate date;
    private String comments;
    private GP gp;

    public Visitation(){
    }

    @Column(name = "date", nullable = false)
    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Column(name = "comments", columnDefinition = "TEXT")
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @ManyToOne
    public GP getGp() {
        return gp;
    }

    public void setGp(GP gp) {
        this.gp = gp;
    }
}
