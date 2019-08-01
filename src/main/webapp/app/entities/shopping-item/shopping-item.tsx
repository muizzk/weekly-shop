import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, InputGroup, Col, Row, Table } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudSearchAction, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getSearchEntities, getEntities } from './shopping-item.reducer';
import { IShoppingItem } from 'app/shared/model/shopping-item.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IShoppingItemProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export interface IShoppingItemState {
  search: string;
}

export class ShoppingItem extends React.Component<IShoppingItemProps, IShoppingItemState> {
  state: IShoppingItemState = {
    search: ''
  };

  componentDidMount() {
    this.props.getEntities();
  }

  search = () => {
    if (this.state.search) {
      this.props.getSearchEntities(this.state.search);
    }
  };

  clear = () => {
    this.setState({ search: '' }, () => {
      this.props.getEntities();
    });
  };

  handleSearch = event => this.setState({ search: event.target.value });

  render() {
    const { shoppingItemList, match } = this.props;
    return (
      <div>
        <h2 id="shopping-item-heading">
          Shopping Items
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Shopping Item
          </Link>
        </h2>
        <Row>
          <Col sm="12">
            <AvForm onSubmit={this.search}>
              <AvGroup>
                <InputGroup>
                  <AvInput type="text" name="search" value={this.state.search} onChange={this.handleSearch} placeholder="Search" />
                  <Button className="input-group-addon">
                    <FontAwesomeIcon icon="search" />
                  </Button>
                  <Button type="reset" className="input-group-addon" onClick={this.clear}>
                    <FontAwesomeIcon icon="trash" />
                  </Button>
                </InputGroup>
              </AvGroup>
            </AvForm>
          </Col>
        </Row>
        <div className="table-responsive">
          {shoppingItemList && shoppingItemList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Owner</th>
                  <th>Name</th>
                  <th>Quantity</th>
                  <th>Origin</th>
                  <th>Deleted</th>
                  <th>Shopping List</th>
                  <th>Category</th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {shoppingItemList.map((shoppingItem, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${shoppingItem.id}`} color="link" size="sm">
                        {shoppingItem.id}
                      </Button>
                    </td>
                    <td>{shoppingItem.owner}</td>
                    <td>{shoppingItem.name}</td>
                    <td>{shoppingItem.quantity}</td>
                    <td>{shoppingItem.origin}</td>
                    <td>{shoppingItem.deleted ? 'true' : 'false'}</td>
                    <td>
                      {shoppingItem.shoppingList ? (
                        <Link to={`shopping-list/${shoppingItem.shoppingList.id}`}>{shoppingItem.shoppingList.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {shoppingItem.category ? <Link to={`category/${shoppingItem.category.id}`}>{shoppingItem.category.id}</Link> : ''}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${shoppingItem.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${shoppingItem.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${shoppingItem.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">No Shopping Items found</div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ shoppingItem }: IRootState) => ({
  shoppingItemList: shoppingItem.entities
});

const mapDispatchToProps = {
  getSearchEntities,
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ShoppingItem);
