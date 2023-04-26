package com.github.rodionovsasha.shoppinglist.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter @Setter
public class ItemDto implements Serializable {
    private static final long serialVersionUID = 2;

    @ApiModelProperty(notes = "Item name", example = "Oranges", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(notes = "Item comment", example = "2 kilograms")
    private String comment;

    @ApiModelProperty(notes = "Item purchase status", example = "false")
    private boolean isBought;

    @ApiModelProperty(notes = "ID of parent items list", example = "1", required = true)
    @NotNull
    private Long listId;

    @ApiModel("Get Item response")
    @NoArgsConstructor
    @Getter @Setter
    public static class GetResponse {
        @ApiModelProperty(notes = "Item ID", example = "45326", required = true)
        private long id;

        @ApiModelProperty(notes = "Item name", example = "Oranges", required = true)
        private String name;

        @ApiModelProperty(notes = "Item comment", example = "2 kilograms")
        private String comment;

        @ApiModelProperty(notes = "Item purchase status", example = "false")
        private boolean isBought;

        @ApiModelProperty(notes = "ID of parent items list", example = "56756", required = true)
        private long listId;
    }

    @ApiModel("Get Item in list response")
    @NoArgsConstructor
    @Getter @Setter
    public static class GetInListResponse {
        @ApiModelProperty(notes = "Item ID", example = "45326", required = true)
        private long id;

        @ApiModelProperty(notes = "Item name", example = "Oranges", required = true)
        private String name;

        @ApiModelProperty(notes = "Item comment", example = "2 kilograms")
        private String comment;

        @ApiModelProperty(notes = "Item purchase status", example = "false")
        private boolean isBought;
    }

    @ApiModel("Create Item response")
    @AllArgsConstructor
    @Getter
    public static class CreateResponse {
        @ApiModelProperty(notes = "ID of newly created Item", example = "45326", required = true)
        private final long id;

        @ApiModelProperty(notes = "ID of parent items list", example = "1", required = true)
        private final long listId;
    }
}
