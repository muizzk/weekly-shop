import { element, by, ElementFinder } from 'protractor';

export default class ShoppingListUpdatePage {
  pageTitle: ElementFinder = element(by.id('weeklyShopApp.shoppingList.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  ownerInput: ElementFinder = element(by.css('input#shopping-list-owner'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setOwnerInput(owner) {
    await this.ownerInput.sendKeys(owner);
  }

  async getOwnerInput() {
    return this.ownerInput.getAttribute('value');
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
