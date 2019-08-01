/* tslint:disable no-unused-expression */
import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import StapleComponentsPage from './staple.page-object';
import { StapleDeleteDialog } from './staple.page-object';
import StapleUpdatePage from './staple-update.page-object';
import { waitUntilDisplayed, waitUntilHidden } from '../../util/utils';

const expect = chai.expect;

describe('Staple e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let stapleUpdatePage: StapleUpdatePage;
  let stapleComponentsPage: StapleComponentsPage;
  let stapleDeleteDialog: StapleDeleteDialog;

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

  it('should load Staples', async () => {
    await navBarPage.getEntityPage('staple');
    stapleComponentsPage = new StapleComponentsPage();
    expect(await stapleComponentsPage.getTitle().getText()).to.match(/Staples/);
  });

  it('should load create Staple page', async () => {
    await stapleComponentsPage.clickOnCreateButton();
    stapleUpdatePage = new StapleUpdatePage();
    expect(await stapleUpdatePage.getPageTitle().getText()).to.match(/Create or edit a Staple/);
    await stapleUpdatePage.cancel();
  });

  it('should create and save Staples', async () => {
    async function createStaple() {
      await stapleComponentsPage.clickOnCreateButton();
      await stapleUpdatePage.setOwnerInput('owner');
      expect(await stapleUpdatePage.getOwnerInput()).to.match(/owner/);
      await stapleUpdatePage.setQuantityInput('quantity');
      expect(await stapleUpdatePage.getQuantityInput()).to.match(/quantity/);
      await stapleUpdatePage.setNameInput('name');
      expect(await stapleUpdatePage.getNameInput()).to.match(/name/);
      await stapleUpdatePage.categorySelectFirstOption();
      await waitUntilDisplayed(stapleUpdatePage.getSaveButton());
      await stapleUpdatePage.save();
      await waitUntilHidden(stapleUpdatePage.getSaveButton());
      expect(await stapleUpdatePage.getSaveButton().isPresent()).to.be.false;
    }

    await stapleComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeCreate = await stapleComponentsPage.countDeleteButtons();
    await createStaple();

    await stapleComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeCreate + 1);
    expect(await stapleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
  });

  it('should delete last Staple', async () => {
    await stapleComponentsPage.waitUntilLoaded();
    const nbButtonsBeforeDelete = await stapleComponentsPage.countDeleteButtons();
    await stapleComponentsPage.clickOnLastDeleteButton();

    const deleteModal = element(by.className('modal'));
    await waitUntilDisplayed(deleteModal);

    stapleDeleteDialog = new StapleDeleteDialog();
    expect(await stapleDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/weeklyShopApp.staple.delete.question/);
    await stapleDeleteDialog.clickOnConfirmButton();

    await stapleComponentsPage.waitUntilDeleteButtonsLength(nbButtonsBeforeDelete - 1);
    expect(await stapleComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
