package npu.edu.shortlink.project.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npu.edu.shortlink.project.common.convention.exception.ServiceException;
import npu.edu.shortlink.project.dao.entity.ShortLinkDO;
import npu.edu.shortlink.project.dao.mapper.ShortLinkMapper;
import npu.edu.shortlink.project.dto.req.ShortLinkCreateReqDTO;
import npu.edu.shortlink.project.dto.resp.ShortLinkCreateRespDTO;
import npu.edu.shortlink.project.service.ShortLinkService;
import npu.edu.shortlink.project.toolkit.HashUtil;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;


    @Value("${short-link.domain.default}")
    private String createShortLinkDefaultDomain;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        // 短链接接口的并发量有多少？如何测试？详情查看：https://nageoffer.com/shortlink/question
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortUrl = StrBuilder.create(createShortLinkDefaultDomain)
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(createShortLinkDefaultDomain)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createdType(requestParam.getCreatedType())
                .validDateType(requestParam.getValidDateType())
                .validDate(requestParam.getValidDate())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .delFlag(0)
                .fullShortUrl(fullShortUrl)
                .build();
        try {
            // 短链接项目有多少数据？如何解决海量数据存储？详情查看：https://nageoffer.com/shortlink/question
            baseMapper.insert(shortLinkDO);

        } catch (DuplicateKeyException ex) {
            // 首先判断是否存在布隆过滤器，如果不存在直接新增
            if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortUrl)) {
                shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
            }
            throw new ServiceException(String.format("短链接：%s 生成重复", fullShortUrl));
        }

        // 删除短链接后，布隆过滤器如何删除？详情查看：https://nageoffer.com/shortlink/question
        shortUriCreateCachePenetrationBloomFilter.add(fullShortUrl);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl("http://" + shortLinkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }


    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        int customGenerateCount = 0;
        String shorUri;
        while (true) {
            if (customGenerateCount > 10) {
                throw new ServiceException("短链接频繁生成，请稍后再试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += UUID.randomUUID().toString();
            // 短链接哈希算法生成冲突问题如何解决？详情查看：https://nageoffer.com/shortlink/question
            shorUri = HashUtil.hashToBase62(originUrl);
            // 判断短链接是否存在为什么不使用Set结构？详情查看：https://nageoffer.com/shortlink/question
            // 如果布隆过滤器挂了，里边存的数据全丢失了，怎么恢复呢？详情查看：https://nageoffer.com/shortlink/question
            if (!shortUriCreateCachePenetrationBloomFilter.contains(createShortLinkDefaultDomain + "/" + shorUri)) {
                break;
            }
            customGenerateCount++;
        }
        return shorUri;
    }
}
