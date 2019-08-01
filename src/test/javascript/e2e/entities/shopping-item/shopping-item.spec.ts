/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import ShoppingItemComponentsPage from './shopping-item.page-object';
import { ShoppingItemDeleteDialog } from './shopping-item.page-object';
import ShoppingItemUpdatePage from './shopping-item-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('ShoppingItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let shoppingItemUpdatePage: ShoppingItemUpdatePage;
  let shoppingItemComponentsPage: ShoppingItemComponentsPage;
  /*let shoppingItemDeleteDialog: ShoppingItemDeleteDialog;*/

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

  it('should load ShoppingItems', async () => {
    await navBarPage.getEntityPage('shopping-item');
    shoppingItemComponentsPage = new ShoppingItemComponentsPage();
    expect(await shoppingItemComponentsPage.getTitle().getText()).to.match(/Shopping Items/);
  });

  it('should load create ShoppingItem page', async () => {
    await shoppingItemComponentsPage.clickOnCreateButton();
    shoppingItemUpdatePage = new ShoppingItemUpdatePage();
    expect(await shoppingItemUpdatePage.getPageTitle().getText()).to.match(/Create or edit a ShoppingItem/);
    await shoppingItemUpdatePage.cancel();
  });

  /* it('should create and save ShoppingItems', async () => {
        async function createShoppingItem() {
            await shoppingItemComponentsPage.clickOnCreateButton();
            await shoppingItemUpdatePage.setOwnerInput('owner');
            expect(await shoppingItemUpdatePage.getOwnerInput()).to.match(/owner/);
            await shoppingItemUpdatePage.setNameInput('name');
            expect(await shoppingItemUpdatePage.getNameInput()).to.match(/name/);
            await shoppingItemUpdatePage.setQuantityInput('quantity');
            expect(await shoppingItemUpdatePage.getQuantityInput()).to.match(/quantity/);
            await shoppingItemUpdatePage.originSelectLastOption();
            const selectedDeleted = await shoppingItemUpdatePage.getDeletedInput().isSelected();
            if (selectedDeleted) {
                await shoppingItemUpdatePage.getDeletedInput().click();
                expect(await shoppingItemUpdatePage.getDeletedInput().isSelected()).to.be.false;
            } else {
                await shoppingItemUpdatePage.getDeletedInput().click();
                expect(await shoppingItemUpdatePage.getDeletedInput().isSelected()).to.be.true;
            }
            await shoppingItemUpdatePage.shoppingListSelectLastOption();
            await shoppingItemUpdatePage.categorySelectFirstOption();
            await waitUntilDisplayed(shoppingItemUpdatePage.getSaveButton());
            await shoppingItemUpdatePage.save();
            await waitUntilHidden(shoppingItemUpdatePage.getSaveButton());
            expect(await shoppingItemUpdatePage.getSaveButton().isPresent()).to.be.false;
        }

        await shoppingItemComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeCreate = await shoppingItemComponentsPage.countDeleteButtons();
        await createShoppingItem();

        await shoppingItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
        expect(await shoppingItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

  it('should delete last ShoppingItem', async () => {
        await shoppingItemComponentsPage.waitUntilLoaded();
        const nbButtonsBeforeDelete = await shoppingItemComponentsPage.countDeleteButtons();
        await shoppingItemComponentsPage.clickOnLastDeleteButton();

        const deleteModal = element(by.className('modal'));
        await waitUntilDisplayed(deleteModal);

        shoppingItemDeleteDialog = new ShoppingItemDeleteDialog();
        expect(await shoppingItemDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/weeklyShopApp.shoppingItem.delete.question/);
        await shoppingItemDeleteDialog.clickOnConfirmButton();

        await shoppingItemComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
        expect(await shoppingItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    }); */

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
