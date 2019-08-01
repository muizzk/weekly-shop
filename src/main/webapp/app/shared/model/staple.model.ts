import { ICategory } from 'app/shared/model/category.model';

export interface IStaple {
  id?: number;
  owner?: string;
  quantity?: string;
  name?: string;
  category?: ICategory;
}

export const defaultValue: Readonly<IStaple> = {};
