import { IShoppingItem } from 'app/shared/model/shopping-item.model';

export interface ICategory {
  id?: number;
  name?: string;
  owner?: string;
  order?: number;
  shoppingItems?: IShoppingItem[];
}

export const defaultValue: Readonly<ICategory> = {};
