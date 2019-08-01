import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shopping-list.reducer';
import { IShoppingList } from 'app/shared/model/shopping-list.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShoppingListDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShoppingListDetail extends React.Component<IShoppingListDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shoppingListEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ShoppingList [<b>{shoppingListEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="owner">Owner</span>
            </dt>
            <dd>{shoppingListEntity.owner}</dd>
          </dl>
          <Button tag={Link} to="/entity/shopping-list" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/shopping-list/${shoppingListEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ shoppingList }: IRootState) => ({
  shoppingListEntity: shoppingList.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShoppingListDetail);
