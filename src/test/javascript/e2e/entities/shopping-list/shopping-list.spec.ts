/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ShoppingListComponentsPage from './shopping-list.page-object';
import { ShoppingListDeleteDialog } from './shopping-list.page-object';
import ShoppingListUpdatePage from './shopping-list-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('ShoppingList e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shoppingListUpdatePage: ShoppingListUpdatePage;
  let shoppingListComponentsPage: ShoppingListComponentsPage;
  let shoppingListDeleteDialog: ShoppingListDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
  });

  it('should load ShoppingLists', async () => {
    await navBarPage.getEntityPage('shopping-list');
    shoppingListComponentsPage = new ShoppingListComponentsPage();
    expect(await shoppingListComponentsPage.getTitle().getText()).to.match(/Shopping Lists/);
  });

  it('should load create ShoppingList page', async () => {
    await shoppingListComponentsPage.clickOnCreateButton();
    shoppingListUpdatePage = new ShoppingListUpdatePage();
    expect(await shoppingListUpdatePage.getPageTitle().getText()).to.match(/Create or edit a ShoppingList/);
    await shoppingListUpdatePage.cancel();
  });

  it('should create and save ShoppingLists', async () => {
    async function createShoppingList() {
      await shoppingListComponentsPage.clickOnCreateButton();
      await shoppingListUpdatePage.setOwnerInput('owner');
      expect(await shoppingListUpdatePage.getOwnerInput()).to.match(/owner/);
      await waitUntilDisplayed(shoppingListUpdatePage.getSaveButton());
      await shoppingListUpdatePage.save();
      await waitUntilHidden(shoppingListUpdatePage.getSaveButton());
      expect(await shoppingListUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await createShoppingList();
    await shoppingListComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await shoppingListComponentsPage.countDeleteButtons();
    await createShoppingList();

    await shoppingListComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await shoppingListComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last ShoppingList', async () => {
    await shoppingListComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await shoppingListComponentsPage.countDeleteButtons();
    await shoppingListComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    shoppingListDeleteDialog = new ShoppingListDeleteDialog();
    expect(await shoppingListDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/weeklyShopApp.shoppingList.delete.question/);
    await shoppingListDeleteDialog.clickOnConfirmButton();

    await shoppingListComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await shoppingListComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
