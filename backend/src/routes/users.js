import { authenticate, users } from "../controllers/v1/users";
import { requireAuthHeader } from "../controllers/v1/users/users.action";

import { wrapAsync } from "../utils/controllers";

module.exports = api => {
  api.route("/v1/authenticate").post(wrapAsync(authenticate));
  api.route("/v1/users").get(requireAuthHeader, wrapAsync(users));
};
