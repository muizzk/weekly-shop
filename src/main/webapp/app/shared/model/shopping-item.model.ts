import { IShoppingList } from 'app/shared/model/shopping-list.model';
import { ICategory } from 'app/shared/model/category.model';

export const enum Origin {
  MANUALLY_ENTERED = 'MANUALLY_ENTERED',
  STAPLE = 'STAPLE',
  RECIPE = 'RECIPE'
}

export interface IShoppingItem {
  id?: number;
  owner?: string;
  name?: string;
  quantity?: string;
  origin?: Origin;
  deleted?: boolean;
  shoppingList?: IShoppingList;
  category?: ICategory;
}

export const defaultValue: Readonly<IShoppingItem> = {
  deleted: false
};
