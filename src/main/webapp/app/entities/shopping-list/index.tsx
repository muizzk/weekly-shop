import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ShoppingList from './shopping-list';
import ShoppingListDetail from './shopping-list-detail';
import ShoppingListUpdate from './shopping-list-update';
import ShoppingListDeleteDialog from './shopping-list-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ShoppingListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ShoppingListUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ShoppingListDetail} />
      <ErrorBoundaryRoute path={match.url} component={ShoppingList} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ShoppingListDeleteDialog} />
  </>
);

export default Routes;
