import { requireAuthHeader } from "../controllers/v1/users/users.action"

import { wrapAsync } from "../utils/controllers";
import { virgilCredentials } from "../controllers/v1/virgil-credentials/virgil-credentials.action";

module.exports = api => {
  api.route("/v1/virgil-credentials").post(requireAuthHeader, wrapAsync(virgilCredentials));
};
