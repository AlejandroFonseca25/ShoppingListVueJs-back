const axios = require('axios');
const { StatusCodes } = require('http-status-codes');
const { expect } = require('chai');

const baseUrlItemsList = 'https://shopping-list-back.azurewebsites.net/api/v1/';
const baseUrlItem = 'https://shopping-list-back.azurewebsites.net/api/v1/item/';

describe('Verifying Getting Process of All Shopping Lists', () => {

    let initialLists = [];
    let listsArrayToGet;
    let numberOfLists;

    before(async () => {
        // Arrange
        const allLists = await axios.get(baseUrlItemsList);
        numberOfLists = allLists.data.length;
        for (let i = 0; i < numberOfLists; i++) {
            const list = await axios.get(`${baseUrlItemsList}itemsList/${allLists.data[i].id}`);
            initialLists.push(list.data);
        }

        for (let i = 0; i < numberOfLists; i++) {
            await axios.delete(`${baseUrlItemsList}itemsList/${allLists.data[i].id}`);
        }

        listsArrayToGet = [{ name: 'My new list 1' }, { name: 'My new list 2' }];

        for (let i = 0; i < listsArrayToGet.length; i++) {
            await axios.post(`${baseUrlItemsList}itemsList/`, listsArrayToGet[i]);
        }
    });

    it('should return all the lists', async () => {
        // Action
        const response = await axios.get(baseUrlItemsList);

        // Assertion
        expect(response.status).to.equal(StatusCodes.OK);
        expect(response.data).to.be.an('array');
        expect(response.data.length).to.equal(2);
        expect(response.data.length).to.equal(listsArrayToGet.length);
        expect(response.data[0]).to.have.property('name');
        expect(response.data[1]).to.have.property('name');
        expect(response.data[0].name).to.equal(listsArrayToGet[0].name);
        expect(response.data[1].name).to.equal(listsArrayToGet[1].name);
    });

    after(async () => {
        const allLists = await axios.get(baseUrlItemsList);
        for (let i = 0; i < allLists.data.length; i++) {
            await axios.delete(`${baseUrlItemsList}itemsList/${allLists.data[i].id}`);
        }

        for (let i = 0; i < numberOfLists; i++) {
            const response = await axios.post(`${baseUrlItemsList}itemsList/`, initialLists[i]);
            for (const item of initialLists[i].items) {
                await axios.post(baseUrlItem,{
                    name: item.name, comment: item.comment, isBought: item.bought, listId: response.data.id
                });
            }
        }
    });
});
