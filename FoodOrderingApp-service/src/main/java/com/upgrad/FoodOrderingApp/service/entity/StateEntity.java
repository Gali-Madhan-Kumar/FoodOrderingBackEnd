package com.upgrad.FoodOrderingApp.service.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "state")
@NamedQueries({
        @NamedQuery(
                name = "getStateByUuid",
                query = "select state from StateEntity state where state.uuid=:stateUuid"
        ),
        @NamedQuery(
                name = "getAllStates",
                query = "select state from StateEntity state"
        ),
})
public class StateEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Size(max = 200)
    @NotNull
    @Column(name = "uuid", unique = true)
    private String uuid;

    @Size(max = 30)
    @Column(name = "state_name")
    private String stateName;

    public StateEntity(@Size(max = 200) @NotNull String uuid, @Size(max = 30) String stateName) {
        this.uuid = uuid;
        this.stateName = stateName;
    }

    public StateEntity() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
