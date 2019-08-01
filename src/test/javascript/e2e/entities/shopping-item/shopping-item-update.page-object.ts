import { element, by, ElementFinder } from 'protractor';

export default class ShoppingItemUpdatePage {
  pageTitle: ElementFinder = element(by.id('weeklyShopApp.shoppingItem.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  ownerInput: ElementFinder = element(by.css('input#shopping-item-owner'));
  nameInput: ElementFinder = element(by.css('input#shopping-item-name'));
  quantityInput: ElementFinder = element(by.css('input#shopping-item-quantity'));
  originSelect: ElementFinder = element(by.css('select#shopping-item-origin'));
  deletedInput: ElementFinder = element(by.css('input#shopping-item-deleted'));
  shoppingListSelect: ElementFinder = element(by.css('select#shopping-item-shoppingList'));
  categorySelect: ElementFinder = element(by.css('select#shopping-item-category'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setOwnerInput(owner) {
    await this.ownerInput.sendKeys(owner);
  }

  async getOwnerInput() {
    return this.ownerInput.getAttribute('value');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setQuantityInput(quantity) {
    await this.quantityInput.sendKeys(quantity);
  }

  async getQuantityInput() {
    return this.quantityInput.getAttribute('value');
  }

  async setOriginSelect(origin) {
    await this.originSelect.sendKeys(origin);
  }

  async getOriginSelect() {
    return this.originSelect.element(by.css('option:checked')).getText();
  }

  async originSelectLastOption() {
    await this.originSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }
  getDeletedInput() {
    return this.deletedInput;
  }
  async shoppingListSelectLastOption() {
    await this.shoppingListSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async shoppingListSelectOption(option) {
    await this.shoppingListSelect.sendKeys(option);
  }

  getShoppingListSelect() {
    return this.shoppingListSelect;
  }

  async getShoppingListSelectedOption() {
    return this.shoppingListSelect.element(by.css('option:checked')).getText();
  }

  async categorySelectFirstOption() {
    await this.categorySelect
      .all(by.tagName('option'))
      .first()
      .click();
  }

  async categorySelectOption(option) {
    await this.categorySelect.sendKeys(option);
  }

  getCategorySelect() {
    return this.categorySelect;
  }

  async getCategorySelectedOption() {
    return this.categorySelect.element(by.css('option:checked')).getText();
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
