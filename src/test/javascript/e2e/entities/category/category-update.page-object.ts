import { element, by, ElementFinder } from 'protractor';

export default class CategoryUpdatePage {
  pageTitle: ElementFinder = element(by.id('weeklyShopApp.category.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#category-name'));
  ownerInput: ElementFinder = element(by.css('input#category-owner'));
  orderInput: ElementFinder = element(by.css('input#category-order'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setOwnerInput(owner) {
    await this.ownerInput.sendKeys(owner);
  }

  async getOwnerInput() {
    return this.ownerInput.getAttribute('value');
  }

  async setOrderInput(order) {
    await this.orderInput.sendKeys(order);
  }

  async getOrderInput() {
    return this.orderInput.getAttribute('value');
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
