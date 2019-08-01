import { IShoppingItem } from 'app/shared/model/shopping-item.model';

export interface IShoppingList {
  id?: number;
  owner?: string;
  shoppingItems?: IShoppingItem[];
}

export const defaultValue: Readonly<IShoppingList> = {};
