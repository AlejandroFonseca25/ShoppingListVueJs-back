const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'https://shopping-list-back.azurewebsites.net/api/v1/itemsList/';
const baseUrlItems = 'https://shopping-list-back.azurewebsites.net/api/v1/item/';


describe('Verifying updating process of a shopping item', () => {

    let itemToUpdate;
    let listIdForItem;

    before(async () => {
        // Arrange
        const responseList = await axios.post(baseUrlItemsList, { name: 'List' });
        listIdForItem = responseList.data.id;

        const responseItem = await axios.post(baseUrlItems, { name: 'Oranges 1kg',comment:'Fruit',isBought: true,listId:listIdForItem});
        itemToUpdate = responseItem.data.id;
    });

    it('should update a item successfully', async () => {
        // Action
        const response = await axios.put(baseUrlItems + itemToUpdate, {
            name: 'Oranges 2kg',
            listId:listIdForItem,
        });

        // Assertion
        expect(response.status).to.equal(StatusCodes.OK);
    });

    it('should return a 404 error when the item does not exist', async () => {
        // Arrange
        const id = '99999';

        try {
            // Action
            await axios.put(baseUrlItems + id, { name: 'Oranges 2kg',listId:listIdForItem });
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
            await axios.put(baseUrlItems + id, { name: 'Oranges 2kg',listId:listIdForItem });
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
        }
    });
    
    it('should return a 400 error when the item name is not provided', async () => {
        // Arrange
        const id = '1';

        try {
            // Action
            await axios.put(baseUrlItems + id, { listId:listIdForItem  });
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('name');
            expect(error.response.data.name).to.equal('no puede estar vacío');
        }
    });

    it('should return a 400 error when the list id of the item is not provided', async () => {
        // Arrange
        const id = '1';

        try {
            // Action
            await axios.put(baseUrlItems + id, { name: 'Oranges 2kg' });
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('listId');
            expect(error.response.data.listId).to.equal('no puede ser null');
        }
    });

    after(async () => {
        await axios.delete(baseUrlItemsList + listIdForItem);
    });
});