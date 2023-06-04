const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'http://localhost:8090/api/v1/itemsList/';

describe('Verifying Adding Process of a Shopping List', () => {

    let listIdToDelete;

    it('should add a list successfully', async () => {
        // Arrange
        const list = {
            name: 'My new list'
        };

        // Action
        const response = await axios.post(baseUrlItemsList, list);

        // Assertion
        expect(response.status).to.equal(StatusCodes.CREATED);
        expect(response.data).to.be.an('object');
        expect(response.data).to.have.property('id');

        listIdToDelete = response.data.id;
    });

    it('should return a 400 error when the list name is not provided', async () => {
        // Arrange
        const list = {};

        try {
            // Action
            await axios.post(baseUrlItemsList, list);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('name');
            expect(error.response.data.name).to.equal('must not be blank');
        }
    });

    after(async () => {
        await axios.delete(baseUrlItemsList + listIdToDelete);
    });
});
