const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'http://localhost:8090/api/v1/itemsList/';

describe('Verifying Updating Process of a Shopping List', () => {

    let listIdToUpdate;

    before(async () => {
        // Arrange
        const list = await axios.post(baseUrlItemsList, { name: 'My new list' });
        listIdToUpdate = list.data.id;
    });

    it('should update a list successfully', async () => {
        // Action
        const response = await axios.put(baseUrlItemsList + listIdToUpdate, {
            name: 'My updated list',
        });

        // Assertion
        expect(response.status).to.equal(StatusCodes.NO_CONTENT);
    });

    it('should return a 404 error when the list does not exist', async () => {
        // Arrange
        const id = '999';

        try {
            // Action
            await axios.put(baseUrlItemsList + id, { name: 'My updated list' });
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
            await axios.put(baseUrlItemsList + id, { name: 'My updated list' });
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
        }
    });

    it('should return a 400 error when the list name is not provided', async () => {
        // Arrange
        const id = '1';
        const emptyList = {};

        try {
            // Action
            await axios.put(baseUrlItemsList + id, emptyList);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('name');
            expect(error.response.data.name).to.equal('must not be blank');
        }
    });

    after(async () => {
        await axios.delete(baseUrlItemsList + listIdToUpdate);
    });
});
