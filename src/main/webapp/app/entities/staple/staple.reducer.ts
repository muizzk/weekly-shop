import axios from 'axios';
import { ICrudSearchAction, ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IStaple, defaultValue } from 'app/shared/model/staple.model';

export const ACTION_TYPES = {
  SEARCH_STAPLES: 'staple/SEARCH_STAPLES',
  FETCH_STAPLE_LIST: 'staple/FETCH_STAPLE_LIST',
  FETCH_STAPLE: 'staple/FETCH_STAPLE',
  CREATE_STAPLE: 'staple/CREATE_STAPLE',
  UPDATE_STAPLE: 'staple/UPDATE_STAPLE',
  DELETE_STAPLE: 'staple/DELETE_STAPLE',
  RESET: 'staple/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IStaple>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type StapleState = Readonly<typeof initialState>;

// Reducer

export default (state: StapleState = initialState, action): StapleState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.SEARCH_STAPLES):
    case REQUEST(ACTION_TYPES.FETCH_STAPLE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_STAPLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_STAPLE):
    case REQUEST(ACTION_TYPES.UPDATE_STAPLE):
    case REQUEST(ACTION_TYPES.DELETE_STAPLE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.SEARCH_STAPLES):
    case FAILURE(ACTION_TYPES.FETCH_STAPLE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_STAPLE):
    case FAILURE(ACTION_TYPES.CREATE_STAPLE):
    case FAILURE(ACTION_TYPES.UPDATE_STAPLE):
    case FAILURE(ACTION_TYPES.DELETE_STAPLE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.SEARCH_STAPLES):
    case SUCCESS(ACTION_TYPES.FETCH_STAPLE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_STAPLE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_STAPLE):
    case SUCCESS(ACTION_TYPES.UPDATE_STAPLE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_STAPLE):
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

const apiUrl = 'api/staples';
const apiSearchUrl = 'api/_search/staples';

// Actions

export const getSearchEntities: ICrudSearchAction<IStaple> = (query, page, size, sort) => ({
  type: ACTION_TYPES.SEARCH_STAPLES,
  payload: axios.get<IStaple>(`${apiSearchUrl}?query=${query}`)
});

export const getEntities: ICrudGetAllAction<IStaple> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_STAPLE_LIST,
  payload: axios.get<IStaple>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IStaple> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_STAPLE,
    payload: axios.get<IStaple>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IStaple> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_STAPLE,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IStaple> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_STAPLE,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IStaple> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_STAPLE,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
