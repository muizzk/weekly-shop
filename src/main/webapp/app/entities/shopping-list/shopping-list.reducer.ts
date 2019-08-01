import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IShoppingList, defaultValue } from 'app/shared/model/shopping-list.model';

export const ACTION_TYPES = {
  SEARCH_SHOPPINGLISTS: 'shoppingList/SEARCH_SHOPPINGLISTS',
  FETCH_SHOPPINGLIST_LIST: 'shoppingList/FETCH_SHOPPINGLIST_LIST',
  FETCH_SHOPPINGLIST: 'shoppingList/FETCH_SHOPPINGLIST',
  CREATE_SHOPPINGLIST: 'shoppingList/CREATE_SHOPPINGLIST',
  UPDATE_SHOPPINGLIST: 'shoppingList/UPDATE_SHOPPINGLIST',
  DELETE_SHOPPINGLIST: 'shoppingList/DELETE_SHOPPINGLIST',
  RESET: 'shoppingList/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IShoppingList>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ShoppingListState = Readonly<typeof initialState>;

// Reducer

export default (state: ShoppingListState = initialState, action): ShoppingListState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_SHOPPINGLISTS):
    case REQUEST(ACTION_TYPES.FETCH_SHOPPINGLIST_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SHOPPINGLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SHOPPINGLIST):
    case REQUEST(ACTION_TYPES.UPDATE_SHOPPINGLIST):
    case REQUEST(ACTION_TYPES.DELETE_SHOPPINGLIST):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_SHOPPINGLISTS):
    case FAILURE(ACTION_TYPES.FETCH_SHOPPINGLIST_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SHOPPINGLIST):
    case FAILURE(ACTION_TYPES.CREATE_SHOPPINGLIST):
    case FAILURE(ACTION_TYPES.UPDATE_SHOPPINGLIST):
    case FAILURE(ACTION_TYPES.DELETE_SHOPPINGLIST):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_SHOPPINGLISTS):
    case SUCCESS(ACTION_TYPES.FETCH_SHOPPINGLIST_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SHOPPINGLIST):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SHOPPINGLIST):
    case SUCCESS(ACTION_TYPES.UPDATE_SHOPPINGLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SHOPPINGLIST):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/shopping-lists';
const apiSearchUrl = 'api/_search/shopping-lists';

// Actions

export const getSearchEntities: ICrudSearchAction<IShoppingList> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_SHOPPINGLISTS,
  payload: axios.get<IShoppingList>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IShoppingList> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SHOPPINGLIST_LIST,
  payload: axios.get<IShoppingList>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IShoppingList> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SHOPPINGLIST,
    payload: axios.get<IShoppingList>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IShoppingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SHOPPINGLIST,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IShoppingList> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SHOPPINGLIST,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IShoppingList> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SHOPPINGLIST,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
