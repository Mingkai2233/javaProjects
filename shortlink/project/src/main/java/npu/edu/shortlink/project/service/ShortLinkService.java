package npu.edu.shortlink.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import npu.edu.shortlink.project.dao.entity.ShortLinkDO;
import npu.edu.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import npu.edu.shortlink.project.dto.resp.ShortLinkCreateRespDTO;

public interface ShortLinkService extends IService<ShortLinkDO> {
    /**
     * 创建短链接
     *
     * @param requestParam 创建短链接请求参数
     * @return 短链接创建信息
     */
    ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam);
}
