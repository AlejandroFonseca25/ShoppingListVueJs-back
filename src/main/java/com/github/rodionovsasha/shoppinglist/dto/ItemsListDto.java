package com.github.rodionovsasha.shoppinglist.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Getter @Setter
public class ItemsListDto implements Serializable {
    private static final long serialVersionUID = 1;

    @ApiModelProperty(notes = "Items List name", example = "Shopping list", required = true)
    @NotBlank
    private String name;

    @ApiModel("Get all lists response")
    @NoArgsConstructor
    @Setter @Getter
    public static class GetAllListsResponse {
        @ApiModelProperty(notes = "Items List ID", example = "45326", required = true)
        private long id;
        @ApiModelProperty(notes = "Items List name", example = "Shopping list", required = true)
        private String name;
    }

    @ApiModel("Get Items List response")
    @NoArgsConstructor
    @Setter @Getter
    public static class GetResponse {
        @ApiModelProperty(notes = "Items List ID", example = "45326", required = true)
        private long id;

        @ApiModelProperty(notes = "Items List name", example = "Shopping list", required = true)
        private String name;

        @ApiModelProperty(notes = "List of items in current Items List")
        private List<ItemDto.GetInListResponse> items;
    }

    @ApiModel("Create Items List response")
    @AllArgsConstructor
    @Getter
    public static class CreateResponse {
        @ApiModelProperty(notes = "ID of newly created Items List", example = "45326", required = true)
        private final long id;
    }
}
