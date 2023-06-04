const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'https://shopping-list-back.azurewebsites.net/api/v1/itemsList/';
const baseUrlItems = 'https://shopping-list-back.azurewebsites.net/api/v1/item/';

describe('Verifying deleting process of a shopping item', () => {

    let itemToDelete;
    let listIdForItem;

    before(async () => {
        // Arrange
        const responseList = await axios.post(baseUrlItemsList, { name: 'List' });
        listIdForItem = responseList.data.id;

        const responseItem = await axios.post(baseUrlItems, { name: 'Orange',comment:'Fruit',isBought: true,listId:listIdForItem});
        itemToDelete = responseItem.data.id;
    });

    it('should delete a item successfully', async () => {
        // Action
        const response = await axios.delete(baseUrlItems + itemToDelete);

        // Assertion
        expect(response.status).to.equal(StatusCodes.NO_CONTENT);
    });

    it('should return a 404 error when the item does not exist', async () => {
        // Arrange
        const id = '99999';

        try {
            // Action
            await axios.delete(baseUrlItems + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.NOT_FOUND);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('message');
            expect(error.response.data.message).to.equal("The entity with id '99999' could not be found");
        }
    });

    it('should return a 400 error when the id is not a number', async () => {
        // Arrange
        const id = 'a';

        try {
            // Action
            await axios.delete(baseUrlItems + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
        }
    });

    after(function () {
        axios.delete(baseUrlItemsList + listIdForItem);
    });
});