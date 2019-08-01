import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './shopping-item.reducer';
import { IShoppingItem } from 'app/shared/model/shopping-item.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShoppingItemDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ShoppingItemDetail extends React.Component<IShoppingItemDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { shoppingItemEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ShoppingItem [<b>{shoppingItemEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="owner">Owner</span>
            </dt>
            <dd>{shoppingItemEntity.owner}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{shoppingItemEntity.name}</dd>
            <dt>
              <span id="quantity">Quantity</span>
            </dt>
            <dd>{shoppingItemEntity.quantity}</dd>
            <dt>
              <span id="origin">Origin</span>
            </dt>
            <dd>{shoppingItemEntity.origin}</dd>
            <dt>
              <span id="deleted">Deleted</span>
            </dt>
            <dd>{shoppingItemEntity.deleted ? 'true' : 'false'}</dd>
            <dt>Shopping List</dt>
            <dd>{shoppingItemEntity.shoppingList ? shoppingItemEntity.shoppingList.id : ''}</dd>
            <dt>Category</dt>
            <dd>{shoppingItemEntity.category ? shoppingItemEntity.category.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/shopping-item" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/shopping-item/${shoppingItemEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ shoppingItem }: IRootState) => ({
  shoppingItemEntity: shoppingItem.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShoppingItemDetail);
