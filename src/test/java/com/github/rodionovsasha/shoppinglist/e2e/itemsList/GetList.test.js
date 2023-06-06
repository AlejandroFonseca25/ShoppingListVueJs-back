const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'https://shopping-list-back.azurewebsites.net/api/v1/itemsList/';

describe('Verifying Getting Process of a Shopping List', () => {

    let listIdToGet;

    before(async () => {
        // Arrange
        const list = { name: 'My new list' };
        const response = await axios.post(baseUrlItemsList, list);
        listIdToGet = response.data.id;
    });

    it('should return a list by id successfully', async () => {
        // Action
        const response = await axios.get(baseUrlItemsList + listIdToGet);

        // Assertion
        expect(response.status).to.equal(StatusCodes.OK);
        expect(response.data).to.be.an('object');
        expect(response.data).to.have.property('id');
        expect(response.data).to.have.property('name');
        expect(response.data.id).to.equal(listIdToGet);
        expect(response.data.name).to.equal('My new list');

    });

    it('should return a 404 error when the list does not exist', async () => {
        // Arrange
        const id = '4';

        try {
            // Action
            await axios.get(baseUrlItemsList + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.NOT_FOUND);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('message');
            expect(error.response.data.message).to.equal("The entity with id '4' could not be found");
        }
    });

    it('should return a 400 error when the id is not a number', async () => {
        // Arrange
        const id = 'a';

        try {
            // Action
            await axios.get(baseUrlItemsList + id);
        } catch (error) {
            // Assertion
            expect(error.response.status).to.equal(StatusCodes.BAD_REQUEST);
            expect(error.response.data).to.be.an('object');
            expect(error.response.data).to.have.property('message');
            expect(error.response.data.message).to.equal("Failed to convert value of type 'java.lang.String' to required type 'long'; nested exception is java.lang.NumberFormatException: For input string: \"a\"");
        }
    });

    after(function () {
        axios.delete(baseUrlItemsList + listIdToGet);
    });
});
