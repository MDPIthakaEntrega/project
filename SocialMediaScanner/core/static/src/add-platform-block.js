var React = require('react');
var Row = require('react-bootstrap').Row;
var Col = require('react-bootstrap').Col;
var Input = require('react-bootstrap').Input;
var Button = require('react-bootstrap').Button;


var AddPlatformBlock = React.createClass({
    cancelHandler: function () {
        this.props.onCancel();
    },

    saveHandler: function () {
        var platform = this.refs.selectInput.getValue();
        var key = this.refs.textInput.getValue();
        this.props.onSave({
            platform,
            key
        })
    },

    render: function () {
        var platforms = this.props.platforms;
        var options = Object.keys(platforms).map(function (platform, idx) {
            if (!platforms[platform]) {
                return <option value={platform} key={idx}>{platform}</option>
            }
        });
        return (
            <div className="add-platform-block">
                <Row>
                    <Col xs={3} md={3}>
                        <Input type="select" placeholder="select" ref="selectInput">
                            {options}
                        </Input>
                    </Col>
                    <Col xs={6} md={6}>
                        <Input
                            type="text"
                            placeholder="Please enter your key or keyword in the platform"
                            ref="textInput"
                        />
                    </Col>
                    <Col xs={3} md={3}>
                        <Button
                            bsStyle="primary"
                            className="add-platform-button"
                            onClick={this.saveHandler}
                            ref="saveButton"
                        >
                            Save
                        </Button>
                        <Button
                            className="add-platform-button"
                            onClick={this.cancelHandler}
                            ref="cancelButton"
                        >
                            Cancel
                        </Button>
                    </Col>
                </Row>
            </div>
        );
    }
});

module.exports = AddPlatformBlock;