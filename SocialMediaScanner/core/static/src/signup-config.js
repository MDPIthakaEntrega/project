var React = require('react/addons');
var Grid = require('react-bootstrap').Grid;
var Row = require('react-bootstrap').Row;
var Col = require('react-bootstrap').Col;
var Button = require('react-bootstrap').Button;
var Input = require('react-bootstrap').Input;
var AddPlatformBlock = require('./add-platform-block');
var $ = require('jquery');

var inputStyle = {
    display: 'block',
    margin: '10px',
    padding: '10px'
};

var buttonStyle = {
    width: '100%'
};

var SignupConfig = React.createClass({
    getInitialState: function () {
        return {
            platforms: {},
            has_unsaved: false
        }
    },

    cancelOnePlatform: function () {
        this.setState({
            has_unsaved: false
        });
    },

    requestPlatformsFromServer: function () {
        $.ajax({
            type: 'GET',
            url: '/api/constants/platforms',
            success: function (data) {
                console.log(data);
                var temp = {};
                for (var i in data.apis) {
                    temp[data.apis[i]] = '';
                }
                this.setState({
                    platforms: temp
                });
            }.bind(this),
            error: function (xhr, status, err) {
            }.bind(this)
        });
    },

    componentDidMount: function () {
        this.requestPlatformsFromServer();
    },

    addOnePlatform: function () {
        this.setState({
            has_unsaved: true
        });
    },

    saveOnePlatform: function (input) {
        var temp = this.state.platforms;
        var platform = input.platform;
        temp[platform] = input.key;
        this.setState({
            platforms: temp,
            has_unsaved: false
        });
    },

    removePlatformHandler: function (platform) {
        var temp = this.state.platforms;
        temp[platform] = '';
        this.setState({
            platform: temp
        });
    },

    render: function () {
        var inputNewPlatform = null;
        if (this.state.has_unsaved) {
            inputNewPlatform = (<AddPlatformBlock
                ref="addBlock"
                onCancel={this.cancelOnePlatform}
                onSave={this.saveOnePlatform}
                {...this.state}
            />);
        }
        var platforms = this.state.platforms;
        var savedPlatforms = Object.keys(platforms).map((platform, idx) => {
            if (platforms[platform]) {
                return (
                    <div style={{margin: '10px'}}>
                        <Row>
                            <Col xs={2}>
                                <p>{platform}</p>
                            </Col>
                            <Col xs={8}>
                                <Input
                                    className="form-control"
                                    type="text"
                                    name={"api-" + platform}
                                    value={platforms[platform]}
                                    readOnly />
                            </Col>
                            <Col xs={2}>
                                <Button onClick={this.removePlatformHandler.bind(this, platform)}>Remove</Button>
                            </Col>
                        </Row>
                    </div>
                );
            }
        });
        var addable = false;
        for (var platform in platforms) {
            if (!platforms[platform]) {
                addable = true;
                break;
            }
        }
        var addButton = addable ? <Button
            ref="addButton"
            bsStyle="success"
            style={buttonStyle}
            onClick={this.addOnePlatform}
        >
            <i className="fa fa-user-plus"></i>
            Add a new platform account (optional)</Button> : null;
        return (
            <div className="input-group" style={inputStyle}>
                {savedPlatforms}
                {inputNewPlatform}
                {addButton}
            </div>
        );
    }
});

module.exports = SignupConfig;

React.render(
    <SignupConfig />,
    document.getElementById('dynamic-api-config')
);


