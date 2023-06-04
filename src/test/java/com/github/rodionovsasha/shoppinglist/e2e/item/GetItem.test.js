const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'https://shopping-list-back.azurewebsites.net/api/v1/itemsList/';
const baseUrlItems = 'https://shopping-list-back.azurewebsites.net/api/v1/item/';

describe('Verifying getting Process of an item', () => {

    let itemId;
    let listId;

    // Arrange
    before(async () => {
        const listResponse = await axios.post(baseUrlItemsList, { name: 'My new list' });
        listId = listResponse.data.id;

        const item = {
            name: 'Milk',
            comment: '',
            isBought: true,
            listId: listId
        };

        const itemResponse = await axios.post(baseUrlItems, item);
        itemId = itemResponse.data.id;
    });

    it('should return an item by id successfully', async () => {
        // Action
        const response = await axios.get(baseUrlItems + itemId);

        // Assertion
        expect(response.status).to.equal(StatusCodes.OK);
        expect(response.data).to.be.an('object');
        expect(response.data).to.have.property('id');
        expect(response.data).to.have.property('name');
        expect(response.data).to.have.property('listId');
        expect(response.data.id).to.equal(itemId);
        expect(response.data.name).to.equal('Milk');
        expect(response.data.listId).to.equal(listId);
    });

    it('should return a 404 error when the item does not exist', async () => {
        // Arrange
        const id = '4';

        try {
            // Action
            await axios.get(baseUrlItems + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.NOT_FOUND);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('message');
            expect(error.response.data.message).to.equal("Item not found");
        }
    });

    it('should return a 400 error when the id is not a number', async () => {
        // Arrange
        const id = 'a';

        try {
            // Action
            await axios.get(baseUrlItems + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
        }
    });

    after(function () {
        axios.delete(baseUrlItems + itemId);
    });
});
