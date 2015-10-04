/**
 * Created by renl on 9/28/15.
 */
/**
 * Created by renl on 9/23/15.
 */
var React = require('react');
var Input = require('react-bootstrap').Input;
var Grid = require('react-bootstrap').Grid;
var Col = require('react-bootstrap').Col;
var Row = require('react-bootstrap').Row;
var Label = require('react-bootstrap').Label;
var Button = require('react-bootstrap').Button;

var AccountConfig = React.createClass({
    getInitialState: function () {
        return {
            api_list: [],
            configs: {}
        };
    },

    componentDidMount: function () {
        this.loadAPIsFromServer();
    },

    handleClick: function () {
        var configs = {};
        for (var apiName in this.refs) {
            configs[apiName] = this.refs[apiName].getValue();
        }
        this.setState({
            api_list: this.state.api_list,
            configs: configs
        });
        $.ajax({
            type: 'POST',
            url: '/api/settings/',
            data: {type: 'account', configs: JSON.stringify(configs)},
            success: function (data) {
                noty({
                    text: 'save account config successfully',
                    layout: 'topRight',
                    type: 'success',
                    killer: true,
                    closeWith: ['click', 'hover']
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },
    // this function will update the config value when you trying to type in
    handleChange: function () {
        var configs = {};
        for (var apiName in this.refs) {
            configs[apiName] = this.refs[apiName].getValue();
        }
        this.setState({
            configs: configs
        });
    },

    loadAPIsFromServer: function () {
        $.ajax({
            type: 'GET',
            url: '/api/settings/?part=account',
            success: function (data) {
                console.log(data);
                this.setState({
                    api_list: data.apis,
                    configs: JSON.parse(data.configs)
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    render: function () {
        if (!this.state.api_list.length) {
            return <div>Loading...</div>
        } else {
            var configs = this.state.api_list.map((apiName) => {
                return (
                    <div>
                        <Col xs={4} md={2}>
                            {apiName}
                        </Col>
                        <Col xs={8} md={4}>
                            <Input
                                value={
                                    this.state.configs.hasOwnProperty(apiName) ?
                                        this.state.configs[apiName] : ""
                                    }
                                type="text"
                                placeholder="Please enter platform username"
                                hasFeedback
                                ref={apiName}
                                groupClassName="group-class"
                                labelClassName="label-class"
                                onChange={this.handleChange}
                            />
                        </Col>
                    </div>
                );
            });
            return (
                <div className="accountConfig">
                    <Grid>
                        <Row>
                            <h2>
                                <Label bsStyle="primary">Review Platform Config</Label>
                            </h2>
                        </Row>
                        <Row>
                        {configs}
                        </Row>
                        <Row>
                            <Col xs={4} md={2}>
                                <Button onClick={this.handleClick} bsStyle="info">Save</Button>
                            </Col>
                        </Row>
                    </Grid>
                </div>
            );
        }
    }
});

module.exports = AccountConfig;
