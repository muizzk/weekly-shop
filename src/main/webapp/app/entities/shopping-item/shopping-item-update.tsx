import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IShoppingList } from 'app/shared/model/shopping-list.model';
import { getEntities as getShoppingLists } from 'app/entities/shopping-list/shopping-list.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { getEntity, updateEntity, createEntity, reset } from './shopping-item.reducer';
import { IShoppingItem } from 'app/shared/model/shopping-item.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer, convertDateTimeToServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IShoppingItemUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IShoppingItemUpdateState {
  isNew: boolean;
  shoppingListId: string;
  categoryId: string;
}

export class ShoppingItemUpdate extends React.Component<IShoppingItemUpdateProps, IShoppingItemUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      shoppingListId: '0',
      categoryId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getShoppingLists();
    this.props.getCategories();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { shoppingItemEntity } = this.props;
      const entity = {
        ...shoppingItemEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/shopping-item');
  };

  render() {
    const { shoppingItemEntity, shoppingLists, categories, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="weeklyShopApp.shoppingItem.home.createOrEditLabel">Create or edit a ShoppingItem</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : shoppingItemEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="shopping-item-id">ID</Label>
                    <AvInput id="shopping-item-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="ownerLabel" for="shopping-item-owner">
                    Owner
                  </Label>
                  <AvField
                    id="shopping-item-owner"
                    type="text"
                    name="owner"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="nameLabel" for="shopping-item-name">
                    Name
                  </Label>
                  <AvField
                    id="shopping-item-name"
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: 'This field is required.' }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="quantityLabel" for="shopping-item-quantity">
                    Quantity
                  </Label>
                  <AvField id="shopping-item-quantity" type="text" name="quantity" />
                </AvGroup>
                <AvGroup>
                  <Label id="originLabel" for="shopping-item-origin">
                    Origin
                  </Label>
                  <AvInput
                    id="shopping-item-origin"
                    type="select"
                    className="form-control"
                    name="origin"
                    value={(!isNew && shoppingItemEntity.origin) || 'MANUALLY_ENTERED'}
                  >
                    <option value="MANUALLY_ENTERED">MANUALLY_ENTERED</option>
                    <option value="STAPLE">STAPLE</option>
                    <option value="RECIPE">RECIPE</option>
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label id="deletedLabel" check>
                    <AvInput id="shopping-item-deleted" type="checkbox" className="form-control" name="deleted" />
                    Deleted
                  </Label>
                </AvGroup>
                <AvGroup>
                  <Label for="shopping-item-shoppingList">Shopping List</Label>
                  <AvInput id="shopping-item-shoppingList" type="select" className="form-control" name="shoppingList.id">
                    <option value="" key="0" />
                    {shoppingLists
                      ? shoppingLists.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="shopping-item-category">Category</Label>
                  <AvInput
                    id="shopping-item-category"
                    type="select"
                    className="form-control"
                    name="category.id"
                    value={isNew ? categories[0] && categories[0].id : shoppingItemEntity.category.id}
                    required
                  >
                    {categories
                      ? categories.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.id}
                          </option>
                        ))
                      : null}
                  </AvInput>
                  <AvFeedback>This field is required.</AvFeedback>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/shopping-item" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  shoppingLists: storeState.shoppingList.entities,
  categories: storeState.category.entities,
  shoppingItemEntity: storeState.shoppingItem.entity,
  loading: storeState.shoppingItem.loading,
  updating: storeState.shoppingItem.updating,
  updateSuccess: storeState.shoppingItem.updateSuccess
});

const mapDispatchToProps = {
  getShoppingLists,
  getCategories,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShoppingItemUpdate);
