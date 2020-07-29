package com.github.userservice.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@ApiModel(description = "Details about the user")
public class User {
    @Id
    @GeneratedValue(generator = "user_id")
    @SequenceGenerator(name = "user_id", sequenceName = "user_id_seq", allocationSize = 1)
    @ApiModelProperty(notes = "id of the user (Unique)")
    Integer id;

    @ApiModelProperty(notes = "name of the user")
    String name;
}
