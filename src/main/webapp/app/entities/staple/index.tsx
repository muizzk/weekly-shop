import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Staple from './staple';
import StapleDetail from './staple-detail';
import StapleUpdate from './staple-update';
import StapleDeleteDialog from './staple-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={StapleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={StapleUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={StapleDetail} />
      <ErrorBoundaryRoute path={match.url} component={Staple} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={StapleDeleteDialog} />
  </>
);

export default Routes;
