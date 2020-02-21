var exec = require('cordova/exec');
/**
 * 登录即时通讯IM的方法
 * @param userName 用户名
 * @param pwd 密码
 */
exports.loginIM = function (userName, pwd, success, error) {
    exec(success, error, 'thsWyyxIM', 'loginIM', [userName,pwd]);
};

/**
 *退出登录
 */
exports.logout = function ( success, error) {
    exec(success, error, 'thsWyyxIM', 'logout', []);
};


/**
 * 获取登录状态
 */
exports.isLoggedIn  = function (success, error) {
    exec(success, error, 'thsWyyxIM', 'isLoggedIn', []);
};

/**
 * 进入房间
 */
exports.enterRoom  = function (roomid,success, error) {
    exec(success, error, 'thsWyyxIM', 'enterRoom', [roomid]);
};

/**
 * 创建频道并进入房间
 */
exports.createChannel  = function (roomid,success, error) {
    exec(success, error, 'thsWyyxIM', 'createChannel', [roomid]);
};









