import { element, by, ElementFinder } from 'protractor';

export default class StapleUpdatePage {
  pageTitle: ElementFinder = element(by.id('weeklyShopApp.staple.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  ownerInput: ElementFinder = element(by.css('input#staple-owner'));
  quantityInput: ElementFinder = element(by.css('input#staple-quantity'));
  nameInput: ElementFinder = element(by.css('input#staple-name'));
  categorySelect: ElementFinder = element(by.css('select#staple-category'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setOwnerInput(owner) {
    await this.ownerInput.sendKeys(owner);
  }

  async getOwnerInput() {
    return this.ownerInput.getAttribute('value');
  }

  async setQuantityInput(quantity) {
    await this.quantityInput.sendKeys(quantity);
  }

  async getQuantityInput() {
    return this.quantityInput.getAttribute('value');
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
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
