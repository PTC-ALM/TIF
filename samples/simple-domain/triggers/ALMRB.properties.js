// <b>ALMRB.properties</b> 
// <p>
// This file contains translatable messages to be presented to the user.
// Do not use this file as a trigger.

# Messages used in the ALM solution.
#
# While the usual purpose for these messages is for internationalization this file is also useful to guarantee that the
# wording is the same regardless of the script.
#
ONLY_PRETRIGGER=This script may be run only from an item changed pre trigger.
ONLY_POSTTRIGGER=This script may be run only from an item changed post trigger.
ONLY_SCHEDULED_TRIGGER=This script may be run only from a scheduled trigger.
NOOP_SECONDARY=Doing nothing since this trigger was fired as a result of another trigger operation.
RESERVED_LABEL=Please choose a different prefix for your label.\n\nLabels starting with \"{0}\" are reserved for system use.
MUST_BE_SEGMENT=The item is not a segment. This script should run only for segments.
MUST_BE_NODE=The item is not a node. This script should run only for nodes.
MUST_BE_NODE_OR_SEGMENT=The item is neither a node nor a segment. This script should run only for nodes or segments.
INVALID_PORTFOLIO_CONTENT=At least one related item ({0,number,#}) is not the correct type. Must be \"{1}\".
CANT_PUT_IN_REVIEW=You may not change the state to \"{0}\" and change the  \"{1}\" field at the same time.\n\nPlease change the state back to its previous value (\"{2}\"), submit your changes, and then edit this item again to change the state.
INCOMPLETE_CO=You may not change the state to \"{0}\" because incomplete task {1} is attached to this document.
INCOMPLETE_COS=You may not change the state to \"{0}\" because the following tasks attached to this document are still incomplete: {1}

# Note the first arg of DIFF_PROJECT is either one of the following two clarification msgs:
AUTH_SAME_PROJECT=Change orders can authorize changes only to items assigned to the same project.
SPAWN_SAME_PROJECT=Change orders must be assigned to the same project as the item from which they are spawned.
DIFF_PROJECT={0}\n\n{1} {2,number,#} is assigned to project {3} but {4} {5,number,#} is assigned to {6}.

# The first arg here is the Type of item being closed or opened:
INVALID_USER_FOR_CO_OPEN=A {0} may be opened only by the user to whom it is assigned, a member of the \"{1}\" group, or a user listed in the \"{2}\" field.
INVALID_USER_FOR_CO_CLOSE=A {0} may be closed only by the user to whom it is assigned or a member of the \"{1}\" group.
INVALID_STATE_FOR_CO_OPEN=This change order may not be opened. It authorizes changes to \"{0}\" {1,number,#} which is in the state {2} (phase {3}) and is therefore not editable.
CANT_REJECT=This change order may not be rejected. It has already been used to authorize changes.
FAILED_DOC_EDITABILITY=You may not change item {0,number,#} because the \"{1}\" field on the document root, item {2,number,#}, is false. Affected field(s): {3}
FAILED_HIER_EDITABILITY=You may not change item {0,number,#}. Field \"{1}\" is false all the way up to the document root. Affected field(s): {2}
AUTH_CHANGES_SEGMENT=Segment {0,number,#}. Previous change: {1}
AUTH_CHANGES_NODE=Operation {4}, user {5}{6}. Field(s) \"{0}\" in node {1,number,#} in segment {2,number,#}. Previous change: {3}
AUTH_CHANGES_NODE_NOFIELDS=Operation {2}, user {3}{4}. Node {0,number,#} in segment {1,number,#}.
AUTH_CHANGES_COPY=Creation of segment {0,number,#} and insertion under node {1} in document {2,number,#}. Previous change: {3}

PROJECT_EXCLUDED_STATE=You may not assign {0} {1,number,#} to project \"{2}\" because field \"{3}\" of its backing item {4,number,#} is set to \"{5}\".
ITEMS_NOT_RETIRED=You may not transition {0} {1,number,#} to state \"{2}\" because at least one item belonging to project \"{3}\" is not in the \"{4}\" phase on field \"{5}\". Items: {6}
PROJ_DOES_NOT_ACCEPT_CRS=Project \"{0}\" does not accept change requests. See field \"{1}\" on item {2,number,#}.
SPAWN_WRONG_PROJECT=Change requests must be assigned to the same project as the item they are spawning. {0} {1,number,#} is in project \"{2}\" while {3} {4,number,#} is in \"{5}\".

CREATESEG_NO_CO=Unable to create new document. Initial state of the document is in the {0} phase which requires change orders, but the target for insertion - ID {1,number,#} - has no valid change order.

CR_UNALLOCATED=Must not transition {0} item {1,number,#} to state {2} if the \"{3}\" relationship is not empty. Please clear the field.
CR_ALLOCATED=Must not transition {0} item {1,number,#} to state {2} if both relationships \"{3}\" and \"{4}\" are empty.

# Triggers used by the ChangeProcessItemProject trigger:
ALREADY_AUTHORIZING_1=Can't switch projects since item {0,number,#} is authorizing work in the former project either through change packages or via the \"{1}\" field.
ALREADY_AUTHORIZING_2=Can't switch projects since certain items are authorizing work in the former project either through change packages or via the \"{0}\" field. Item list: {1}.
ALREADY_AUTHORIZING_N=Can't switch projects since a large number of items are authorizing work in the former project either through change packages or via the \"{0}\" field.
NEED_OWNER=Items of type \"{0}\" must have an owner in the project hierarchy.
MUST_BE_TOP_LEVEL=Items of type \"{0}\" can only be attached at the top level in the project hierarchy.
INCONSISTENT_PROJECTS=New project \"{0}\" is inconsistent with that of the new container, ID {1,number,#}, which belongs to project \"{2}\".
MUST_BE_SAME_PROJECT=Items of type \"{0}\" must belong to the same project as their containing item.
CANT_DETACH=You must not detach item {0,number,#} from {1,number,#} since it must be connected always to a parent in the project hierarchy.

CLOSE_ASOF_DATE1=You must not set the "Tests As Of Date" into the future. You may get this error if your system clock is significantly ahead of the database server clock. You were ahead by only {0} seconds.

CLOSE_ASOF_DATE1=You must not set the "Tests As Of Date" into the future. You may get this error if your system clock is ahead of the database server clock. You were ahead by only {0} seconds.
CLOSE_ASOF_DATE2=You must not set the "Tests As Of Date" into the future. You may get this error if your system clock is ahead of the database server clock. You were ahead by about {0} minutes.
WRONG_ASOF_DATE=You must not set the "Tests As Of Date" into the future.
