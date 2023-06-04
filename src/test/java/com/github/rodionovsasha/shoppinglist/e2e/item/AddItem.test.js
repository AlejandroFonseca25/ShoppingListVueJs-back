const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'https://shopping-list-back.azurewebsites.net/api/v1/itemsList/';
const baseUrlItems = 'https://shopping-list-back.azurewebsites.net/api/v1/item/';

describe('Verifying adding Process of a Shopping item', () => {

    let itemId;
    let listId;

    // Arrange
    before(async () => {
        const response = await axios.post(baseUrlItemsList, { name: 'My new list' });
        listId = response.data.id;
    });

    it('should add an item successfully', async () => {
        //Arrange
        const item = {
            name: 'Cheese',
            comment: '',
            isBought: false,
            listId: listId
        };

        // Action
        const response = await axios.post(baseUrlItems, item);

        // Assertion
        expect(response.status).to.equal(StatusCodes.CREATED);
        expect(response.data).to.be.an('object');
        expect(response.data).to.have.property('id');
        expect(response.data).to.have.property('listId');
        expect(response.data.listId).to.equal(listId);

        itemId = response.data.id;
    });

    it('should return a 400 error when the item name is not provided', async () => {
        // Arrange
        const item = {
            name: '',
            comment: 'Cheddar cheese',
            isBought: false,
            listId: listId
        };

        try {
            // Action
            await axios.post(baseUrlItems, item);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('name');
            expect(error.response.data.name).to.equal('no puede estar vacío');
        }
    });

    it('should return a 400 error when listId is null', async () => {
        // Arrange
        const item = {
            name: 'Cheese',
            comment: 'Cheddar cheese',
            isBought: false,
            listId: null
        };

        try {
            // Action
            await axios.post(baseUrlItems, item);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('listId');
            expect(error.response.data.listId).to.equal('no puede ser null');
        }
    });

    it('should return a 415 error when item object is not provided', async () => {
        // Arrange
        const item = null;

        try {
            // Action
            await axios.post(baseUrlItems, item);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.UNSUPPORTED_MEDIA_TYPE);
        }
    });

    after(async () => {
        await axios.delete(baseUrlItems + itemId);
    });
});
