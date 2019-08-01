import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './staple.reducer';
import { IStaple } from 'app/shared/model/staple.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IStapleDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class StapleDetail extends React.Component<IStapleDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { stapleEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Staple [<b>{stapleEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="owner">Owner</span>
            </dt>
            <dd>{stapleEntity.owner}</dd>
            <dt>
              <span id="quantity">Quantity</span>
            </dt>
            <dd>{stapleEntity.quantity}</dd>
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{stapleEntity.name}</dd>
            <dt>Category</dt>
            <dd>{stapleEntity.category ? stapleEntity.category.id : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/staple" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/staple/${stapleEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ staple }: IRootState) => ({
  stapleEntity: staple.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(StapleDetail);
