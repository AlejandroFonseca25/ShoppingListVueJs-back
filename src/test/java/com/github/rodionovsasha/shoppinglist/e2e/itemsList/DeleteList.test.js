const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'https://shopping-list-back.azurewebsites.net/api/v1/itemsList/';

describe('Verifying Deleting Process of a Shopping List', () => {

    let listIdToDelete;

    before(async () => {
        // Arrange
        const response = await axios.post(baseUrlItemsList, { name: 'My new list' });
        listIdToDelete = response.data.id;
    });

    it('should delete a list successfully', async () => {
        // Action
        const response = await axios.delete(baseUrlItemsList + listIdToDelete);

        // Assertion
        expect(response.status).to.equal(StatusCodes.NO_CONTENT);
    });

    it('should return a 404 error when the list does not exist', async () => {
        // Arrange
        const id = '999';

        try {
            // Action
            await axios.delete(baseUrlItemsList + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.NOT_FOUND);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('message');
            expect(error.response.data.message).to.equal("The entity with id '999' could not be found");
        }
    });

    it('should return a 400 error when the id is not a number', async () => {
        // Arrange
        const id = 'a';

        try {
            // Action
            await axios.delete(baseUrlItemsList + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
        }
    });
});
