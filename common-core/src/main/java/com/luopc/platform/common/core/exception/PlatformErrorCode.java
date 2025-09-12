package com.luopc.platform.common.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Common Error Codes
 *
 * @author Platform Team
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum PlatformErrorCode implements ErrorCode {

    // Success
    SUCCESS(200, "Success"),
    CREATED(201, "创建成功"),
    ACCEPTED(202, "请求成功"),
    PARTIAL_SUCCESS(206, "部分成功"),

    // Client errors (4xx)
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    VALIDATION_FAILED(422, "Validation Failed"),
    TOO_MANY_REQUESTS(429, "Too Many Requests"),
    BAD_METHOD(430, "Bad Method Request"),
    NOT_ACCEPTABLE(431, "Service Unavailable"),
    REQUEST_TIMEOUT(432, "Request Timeout"),
    REQUEST_CONFLICT(433, "Duplicate Request"),
    NOT_READABLE(434, "Resource Not Readable"),
    RETRY(440, "Action failed, please retry"),
    FAILED(441, "Action failed"),
    PARAM_ERROR(442, "Parameter error"),
    INVALID_PARAM_EXIST(443, "Parameter exists"),
    INVALID_PARAM_EMPTY(444, "Parameter is empty"),
    PARAM_TYPE_MISMATCH(445, "Parameter type mismatch"),
    PARAM_VALID_ERROR(446, "Parameter validation error"),
    ILLEGAL_REQUEST(447, "Illegal request"),
    INVALID_VERIFICATION_CODE(450, "验证码错误"),
    INVALID_USERNAME_PASSWORD(451, "Invalid username/password"),
    INVALID_RE_PASSWORD(452, "Invalid re-password"),
    INVALID_OLD_PASSWORD(453, "Invalid old password"),
    USERNAME_ALREADY_IN(454, "Username already exists"),
    INVALID_USERNAME(455, "Invalid username"),
    INVALID_ROLE(456, "Role invalid"),
    ROLE_CONFLICT(460, "Role conflict"),
    USER_STATUS_ERROR(461, "User status error"),

    // Server errors (5xx)
    SYSTEM_ERROR(500, "Internal Server Error"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    REMOTE_UNAVAILABLE(509, "Remote service unavailable"),
    RESOURCE_NO_FOUND(510, "Resource not found"),
    FILE_NO_FOUND(511, "File not found"),
    FILE_UN_READABLE(512, "File not readable"),
    FILE_UN_WRITABLE(513, "File not writable"),
    FILE_UN_DELETABLE(514, "File not deletable"),

    // Business errors (6xxx)
    BUSINESS_ERROR(6000, "Business Error"),
    PARAMETER_INVALID(6001, "Parameter Invalid"),
    DATA_NOT_FOUND(6002, "Data Not Found"),
    DATA_ALREADY_EXISTS(6003, "Data Already Exists"),
    OPERATION_NOT_ALLOWED(6004, "Operation Not Allowed"),
    OPERATION_FAILED(6005, "Operation Failed"),

    // Authentication & Authorization errors (7xxx)
    TOKEN_INVALID(7001, "Token Invalid"),
    TOKEN_EXPIRED(7002, "Token Expired"),
    PERMISSION_DENIED(7003, "Permission Denied"),
    ACCOUNT_LOCKED(7004, "Account Locked"),
    ACCOUNT_DISABLED(7005, "Account Disabled"),

    // External service errors (8xxx)
    EXTERNAL_SERVICE_ERROR(8000, "External Service Error"),
    NETWORK_ERROR(8001, "Network Error"),
    TIMEOUT_ERROR(8002, "Timeout Error"),


    // System errors (9xxx)
    SYSTEM_BUSY(9001, "System Busy"),
    SYSTEM_MAINTENANCE(9002, "System Under Maintenance"),
    CONFIGURATION_ERROR(9003, "Configuration Error");

    private final int code;
    private final String message;
}
